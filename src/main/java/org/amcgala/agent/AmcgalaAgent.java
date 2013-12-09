package org.amcgala.agent;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.Procedure;
import scala.concurrent.duration.FiniteDuration;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public abstract class AmcgalaAgent extends UntypedActor {

    final protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected Random random = new Random(System.nanoTime());

    private World.JCell currentCell;

    private World.Index currentPosition;

    protected final Agent.AgentID id = new Agent.AgentID(getSelf().hashCode());

    private boolean localMode = getContext().system().settings().config().getBoolean("org.amcgala.agent.simulation.local-mode");

    private final String simulationManagerPath = localMode ? getContext().system().settings().config().getString("org.amcgala.agent.simulation.local-address") : getContext().system().settings().config().getString("org.amcgala.agent.simulationManager.remote-address");


    private final ActorSelection simulationManager = getContext().actorSelection(simulationManagerPath);
    private ActorRef simulation = ActorRef.noSender();

    private final Cancellable waitTask = getContext().system().scheduler().scheduleOnce(new FiniteDuration(10, TimeUnit.SECONDS), new Runnable() {
        @Override
        public void run() {
            simulation.tell(Simulation.RegisterWithDefaultIndex$.MODULE$, getSelf());
            getContext().become(updateHandling);
        }
    }, getContext().system().dispatcher());

    private final Procedure<Object> waitForPosition = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof AgentMessages.SpawnAt) {

                AgentMessages.SpawnAt spawnMessage = (AgentMessages.SpawnAt) message;
                simulation.tell(new Simulation.Register(spawnMessage.position()), getSelf());
                waitTask.cancel();
                getContext().become(updateHandling);
            } else if (message instanceof AgentMessages.SpawnRejected$) {
                getContext().stop(getSelf());
            } else {
                unhandled(message);
            }
        }
    };

    private final Procedure<Object> updateHandling = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof Simulation.SimulationUpdate) {
                Simulation.SimulationUpdate update = (Simulation.SimulationUpdate) message;

                currentCell = update.currentCell();
                currentPosition = update.currentPosition();
                AgentMessages.AgentMessage decision = onUpdate(update);
                simulation.tell(decision, getSelf());
            } else {
                unhandled(message);
            }
        }
    };

    private final Procedure<Object> waitForSimulation = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof SimulationManager.SimulationResponse$) {
                simulation = getSender();
                getContext().become(waitForPosition);
            } else {
                unhandled(message);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
    }

    @Override
    public void preStart() throws Exception {
        simulationManager.tell(SimulationManager.SimulationRequest$.MODULE$, getSelf());
        getContext().become(waitForSimulation);
    }

    @Override
    public void postStop() {
        tellSimulation(AgentMessages.Death$.MODULE$);
    }

    /**
     * Erzeugt ein Kind und platziert es auf dem selben Index wie der Vater-Agent.
     */
    protected void spawnChild() {
        Props props = Props.create(new AmcgalaAgentCreator(this.getClass()));
        ActorRef ref = getContext().system().actorOf(props);
        ref.tell(new AgentMessages.SpawnAt(currentPosition), getSelf());
    }

    /**
     * Erzeugt ein Kind und platziert dieses auf dem angegebenen Index in der Welt.
     *
     * @param index der Index, auf dem das Kind platziert werden soll
     */
    protected void spawnChild(World.Index index) {
        Props props = Props.create(new AmcgalaAgentCreator(this.getClass()));
        ActorRef ref = getContext().system().actorOf(props);
        ref.tell(SimulationManager.SimulationResponse$.MODULE$, simulation);
        ref.tell(new AgentMessages.SpawnAt(index), getSelf());
    }

    /**
     * Beendet diesen Agenten und teilt dies der Simulation mit.
     *
     * @return die Stop-Meldung an die Simulation
     */
    protected AgentMessages.AgentMessage die() {
        return AgentMessages.Death$.MODULE$;
    }

    /**
     * Sendet eine Erfolgsmeldung an einen Elternagenten, um diesem mitzuteilen, dass eine Aufgabe erfolgreich
     * abgeschlossen wurde.
     */
    protected void success() {
        getContext().parent().tell(AgentMessages.Success$.MODULE$, getSelf());
    }

    /**
     * Sendet eine Fehlermeldung an einen Elternagenten, um diesem mitzuteilen, dass eine Aufgabe nicht erfolgreich
     * abgeschlossen werden konnte.
     */
    protected void failure() {
        getContext().parent().tell(AgentMessages.Failure$.MODULE$, getSelf());
    }

    /**
     * Erzeugt einen Kindsagenten und setzt in an die angegebene Position.
     *
     * @param x die x-Koordinate
     * @param y die y-Koordinate
     */
    protected void spawnAt(int x, int y) {
        getSelf().tell(new AgentMessages.SpawnAt(new World.Index(x, y)), getSelf());
    }

    /**
     * Waehlt einen zufaellig eine Nachbarzelle aus.
     *
     * @param update die aktuelle Umgebung
     * @return die NAchbarzelle
     */
    protected World.JCellWithIndex getRandomNeighbour(Simulation.SimulationUpdate update) {
        return update.neighbours().values().toArray(new World.JCellWithIndex[1])[random.nextInt(update.neighbours().size())];
    }

    protected World.JCellWithIndex getNeighbour(Direction direction, Simulation.SimulationUpdate update) {
        return update.neighbours().get(direction.relativeIndex());
    }


    /**
     * Bewegt den Agenten auf ein benachbartes Feld.
     *
     * @param relativeIndex der relative Index des Nachbarn
     * @param update        das SimulationUpdate Objekt
     * @return die Nachricht, die an die Simulation gesendet wird
     */
    protected AgentMessages.AgentMessage moveTo(World.Index relativeIndex, Simulation.SimulationUpdate update) {
        requestUpdate();
        return new AgentMessages.MoveTo(update.neighbours().get(relativeIndex).index());
    }

    /**
     * Bewegt den Agenten auf ein benachbartes Feld.
     *
     * @param index der absoluten Index des Nachbarn
     * @return die Nachricht, die an die Simulation gesendet wird
     */
    protected AgentMessages.AgentMessage moveTo(World.Index index) {
        requestUpdate();
        return new AgentMessages.MoveTo(index);
    }

    /**
     * Bewegt den Agenten in die angebenene Richtung.
     *
     * @param direction die Richtung, in die der Agent gehen soll
     * @param update    der aktuelle Zustand der Agentenumgebung
     * @return die AgentMessage, die an die Simulation geschickt wird
     */
    protected AgentMessages.AgentMessage moveTo(Direction direction, Simulation.SimulationUpdate update) {
        requestUpdate();
        return new AgentMessages.MoveTo(update.neighbours().get(direction.relativeIndex()).index());
    }


    /**
     * Aendert den Wert der Zelle, auf der der Agent steht.
     *
     * @param value der neue Wert
     * @return die AgentMessage, die an die Simulation geschickt wird
     */
    protected AgentMessages.AgentMessage changeValue(float value) {
        requestUpdate();
        return new AgentMessages.ChangeValue(value);
    }

    /**
     * Legt ein InformationObject auf der aktuellen Zelle ab.
     *
     * @param informationObject das InformationObject
     * @return die AgentMessage, die an die Simulation geschickt wird
     */
    protected AgentMessages.AgentMessage putInformationObject(World.InformationObject informationObject) {
        requestUpdate();
        return new AgentMessages.PutInformationObject(informationObject);
    }

    /**
     * Legt ein InformationObject auf einer benachbarten Zelle ab.
     *
     * @param index             der Index der Nachbarzelle
     * @param informationObject das InformationObject
     * @return die AgentMessage, die an die Simulation geschickt wird
     */
    protected AgentMessages.AgentMessage putInformationObjectTo(World.Index index, World.InformationObject informationObject) {
        requestUpdate();
        return new AgentMessages.PutInformationObjectTo(index, informationObject);
    }

    /**
     * Legt ein InformationObject auf der aktuellen Zelle ab.
     *
     * @param update das InformationObject
     * @return die AgentMessage, die an die Simulation geschickt wird
     */
    protected AgentMessages.AgentMessage putVisitedObject(Simulation.SimulationUpdate update) {
        requestUpdate();
        return new AgentMessages.PutInformationObjectTo(update.currentPosition(), World.Visited$.MODULE$);
    }


    /**
     * Prueft, ob die Nachbarzelle schon besucht wurde
     *
     * @param direction die Richtung, in der die zu untersuchende Zelle liegt
     * @param update    der aktuelle Zustand der Agentenumgebung
     * @return true, wenn Zelle schon besucht wurde
     */
    protected boolean checkVisited(Direction direction, Simulation.SimulationUpdate update) {
        return checkVisited(update.neighbours().get(direction.relativeIndex()).cell());
    }

    /**
     * Prueft, ob die Zelle schon besucht wurde.
     *
     * @param cell die zu untersuchende Zelle
     * @return true, wenn Zelle schon besucht wurde
     */
    protected boolean checkVisited(World.JCell cell) {
        boolean visited = false;
        for (World.InformationObject informationObject : cell.informationObjects()) {
            visited = (informationObject instanceof World.Visited$) || visited;
        }
        return visited;
    }


    /**
     * Gibt den absoluten Index einer Nachbarzelle zurueck.
     *
     * @param direction die Richtung, in der die Zelle liegt
     * @param update    der aktuelle Zustand der Agentenumgebung
     * @return der absolute Index der Zelle
     */
    protected World.Index getNeighbourIndex(Direction direction, Simulation.SimulationUpdate update) {
        return getNeighbour(direction, update).index();
    }

    /**
     * Triggert ein SimulationUpdate, das dem Agenten nach einer Aktion geschickt wird.
     * Diese Methode hat nur eine Wirkung, wenn push-mode in der Konfiguration auf false gesetzt wird.
     */
    private void requestUpdate() {
        getContext().system().scheduler().scheduleOnce(new FiniteDuration(1, TimeUnit.MILLISECONDS), new Runnable() {
            @Override
            public void run() {
                simulation.tell(Simulation.RequestUpdate$.MODULE$, getSelf());
            }
        }, getContext().system().dispatcher());
    }

    protected void tellSimulation(AgentMessages.AgentMessage message) {
        simulation.tell(message, getSelf());
    }


    /**
     * Callback Methode, die bei jedem Update der Simulation aufgerufen wird.
     * Hier gehört der Code rein, der von einem Agenten ausgeführt werden soll.
     *
     * @param update die Update Informationen
     * @return die Aktion des Agenten
     */
    abstract protected AgentMessages.AgentMessage onUpdate(Simulation.SimulationUpdate update);

    static class AmcgalaAgentCreator implements Creator<Actor> {
        Class<? extends AmcgalaAgent> childClass;

        AmcgalaAgentCreator(Class<? extends AmcgalaAgent> childClass) {
            this.childClass = childClass;
        }

        public Actor create() throws Exception {
            return childClass.getConstructor().newInstance();
        }
    }
}


