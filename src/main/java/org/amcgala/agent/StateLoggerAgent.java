package org.amcgala.agent;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import scala.Tuple2;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A StateLoggerAgent receives a SimulationUpdate every time something changes in the simulated world. It provides all
 * necessary information that is needed for a visualisation of the simulation.
 */
public abstract class StateLoggerAgent extends UntypedActor {
    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    protected final Framework framework = Framework.getInstance(FrameworkMode.SOFTWARE);

    private Map<World.Index, World.Cell> cells = new HashMap<>();
    private Map<Agent.AgentID, Agent.AgentStates> agents = new HashMap<>();

    protected int worldWidth;
    protected int worldHeight;
    protected int scaleX;
    protected int scaleY;

    private boolean localMode = getContext().system().settings().config().getBoolean("org.amcgala.agent.simulation.local-mode");

    private final String simulationManagerPath = localMode ? getContext().system().settings().config().getString("org.amcgala.agent.simulation.local-address") : getContext().system().settings().config().getString("org.amcgala.agent.simulation.remote-address");
    private ActorSelection simulationManager = getContext().actorSelection(simulationManagerPath);

    private ActorRef simulation = ActorRef.noSender();

    private final Procedure<Object> waitForSimulation = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof SimulationManager.SimulationResponse$) {
                simulation = getSender();
                simulation.tell(Simulation.RegisterStateLogger$.MODULE$, getSelf());
                getContext().become(handleUpdates);
            } else {
                unhandled(message);
            }
        }
    };


    private final Procedure<Object> handleUpdates = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof Simulation.SimulationState) {
                System.out.println("Received State!");
                Simulation.SimulationState state = (Simulation.SimulationState) message;

                worldWidth = state.worldInfo().width();
                worldHeight = state.worldInfo().height();
                scaleX = framework.getWidth() / worldWidth;
                scaleY = framework.getHeight() / worldHeight;

                cells.clear();
                for (Tuple2<World.Index, World.Cell> entry : state.worldInfo().cells()) {
                    cells.put(entry._1(), entry._2());
                }
                agents.clear();
                for (Agent.AgentStates as : state.agents()) {
                    agents.put(as.id(), as);
                }

                onInit();

                getContext().system().scheduler().schedule(
                        Duration.create(200, TimeUnit.MILLISECONDS),
                        Duration.create(200, TimeUnit.MILLISECONDS),
                        getSelf(),
                        Simulation.Update$.MODULE$,
                        getContext().system().dispatcher(),
                        ActorRef.noSender()
                );
            } else if (message instanceof Simulation.SimulationStateUpdate) {
                Simulation.SimulationStateUpdate state = (Simulation.SimulationStateUpdate) message;

                agents.clear();
                for (Agent.AgentStates as : state.agents()) {
                    agents.put(as.id(), as);
                }

                cells.clear();
                for (Tuple2<World.Index, World.Cell> entry : state.changedCells()) {
                    cells.put(entry._1(), entry._2());
                }

            } else if (message instanceof Simulation.AgentStateChange) {
                Simulation.AgentStateChange state = (Simulation.AgentStateChange) message;
                agents.put(state.state().id(), state.state());
            } else if (message instanceof Simulation.CellChange) {
                Simulation.CellChange change = (Simulation.CellChange) message;
                cells.put(change.index(), change.cell());
            } else if (message instanceof Simulation.Update$) {
                onUpdate(cells, agents);
            } else if (message instanceof Simulation.AgentDeath) {
                Simulation.AgentDeath death = (Simulation.AgentDeath) message;
                agents.remove(death.state().id());
            } else if (message instanceof AgentMessages.NextRound$) {
                System.out.println("Resetting Statelogger and waiting for new simulation...");
                onReset();
                simulation = ActorRef.noSender();
                simulationManager.tell(SimulationManager.SimulationRequest$.MODULE$, getSelf());
                getContext().become(waitForSimulation);
            } else {
                unhandled(message);
            }
        }
    };


    /**
     * User overridable callback.
     * <p/>
     * Is called when an Actor is started.
     * Actors are automatically started asynchronously when created.
     */
    @Override
    public void preStart() throws Exception {
        simulationManager.tell(SimulationManager.SimulationRequest$.MODULE$, getSelf());
        getContext().become(waitForSimulation);
    }

    @Override
    public void onReceive(Object message) throws Exception {
    }

    public void onReset(){}

    /**
     * Wird waehrend der Instantiierung des Objekts aufgerufen. Kann verwendet werden, um einmal auszufuehrende Logik
     * (z.B. Initialisierung von Variablen) zu definieren.
     */
    abstract public void onInit();

    /**
     * Wird einmal pro Tick aufgerufen.
     * @param cells Map aller Zellen der darstellbaren Welt
     * @param agents Map aller in der Welt lebenden Agenten
     */
    abstract public void onUpdate(Map<World.Index, World.Cell> cells, Map<Agent.AgentID, Agent.AgentStates> agents);
}
