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
                Agent.SpawnAt spawnMessage = (Agent.SpawnAt) message;
                log.debug("Received Spawn Instructions from parent: " + spawnMessage.position());
                simulation.tell(new Simulation.Register(spawnMessage.position()), getSelf());
                waitTask.cancel();
                getContext().become(updateHandling);
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
    public void preStart() throws Exception {
        getContext().become(waitForPosition);
    }

    @Override
    public void onReceive(Object message) throws Exception {
    }

    // TODO Index Parameter
    protected void spawnChild() {
        log.debug("Spawning Child");
        Props props = Props.create(new AmcgalaAgentCreator(this.getClass()));
        ActorRef ref = getContext().system().actorOf(props);
        ref.tell(new Agent.SpawnAt(currentState.position()), getSelf());
        log.debug("Telling Child to spawn at " + currentState.position());
    }

    protected Agent.AgentMessage die() {
        simulation.tell(Agent.Death$.MODULE$, getSelf());
        getContext().stop(getSelf());
        return Agent.Death$.MODULE$;
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


