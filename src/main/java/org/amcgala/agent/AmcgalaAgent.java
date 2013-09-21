package org.amcgala.agent;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.Procedure;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;


public abstract class AmcgalaAgent extends UntypedActor {

    final protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected Agent.AgentState currentState;

    protected final Agent.AgentID id = new Agent.AgentID(getSelf().hashCode());

    private final String simulationPath = getContext().system().settings().config().getString("org.amcgala.agent.simulation");

    private final ActorSelection simulation = getContext().actorSelection(simulationPath);

    private final Cancellable waitTask = getContext().system().scheduler().scheduleOnce(new FiniteDuration(5, TimeUnit.SECONDS), new Runnable() {
        @Override
        public void run() {
            simulation.tell(Simulation.RegisterWithDefaultIndex$.MODULE$, getSelf());
            getContext().become(updateHandling);
        }
    }, getContext().system().dispatcher());

    private final Procedure<Object> waitForPosition = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof Agent.SpawnAt) {
                log.info("Received a spawn at");
                Agent.SpawnAt spawnMessage = (Agent.SpawnAt) message;
                simulation.tell(new Simulation.Register(spawnMessage.position()), getSelf());
                waitTask.cancel();
                getContext().become(updateHandling);
            } else if (message instanceof Agent.SpawnRejected$) {
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
                log.debug("Received a SimulationUpdate! Processing...");
                currentState = ((Simulation.SimulationUpdate) message).currentState();
                sender().tell(onUpdate((Simulation.SimulationUpdate) message), self());
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
        getContext().become(waitForPosition);
    }

    protected void spawnChild() {
        Props props = Props.create(new AmcgalaAgentCreator(this.getClass()));
        ActorRef ref = getContext().system().actorOf(props);
        ref.tell(new Agent.SpawnAt(currentState.position()), getSelf());
    }

    protected void spawnChild(World.Index index) {
        Props props = Props.create(new AmcgalaAgentCreator(this.getClass()));
        ActorRef ref = getContext().system().actorOf(props);
        ref.tell(new Agent.SpawnAt(index), getSelf());
    }

    protected Agent.AgentMessage die() {
        simulation.tell(Agent.Death$.MODULE$, getSelf());
        getContext().stop(getSelf());
        return Agent.Death$.MODULE$;
    }

    protected void success() {
        getContext().parent().tell(Agent.Success$.MODULE$, getSelf());
    }

    protected void failure() {
        getContext().parent().tell(Agent.Failure$.MODULE$, getSelf());
    }

    protected void spawnAt(int x, int y) {
        getSelf().tell(new Agent.SpawnAt(new World.Index(x, y)), getSelf());
    }

    abstract protected Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update);

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


