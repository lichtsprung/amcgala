package org.amcgala.agents;

import akka.actor.Actor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

/**
 * An Agent
 * <ul>
 * <li>receives its currentState vicinity in every time step</li>
 * </ul>
 */
public abstract class AmcgalaAgent extends UntypedActor {

    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected Agent.AgentState currentState;
    protected World.Index initialIndex;

    // TODO Shouldn't be hardcoded!
    private ActorSelection simulation = getContext().system().actorSelection("akka.tcp://Simulator@localhost:2552/user/simulation");


    public AmcgalaAgent(World.Index initialIndex) {
        this.initialIndex = initialIndex;
    }

    /**
     * User overridable callback.
     * <p/>
     * Is called when an Actor is started.
     * Actor are automatically started asynchronously when created.
     * Empty default implementation.
     */
    @Override
    public void preStart() throws Exception {
        if (initialIndex == World$.MODULE$.RandomIndex()) {
            simulation.tell(Simulation.RegisterWithRandomIndex$.MODULE$, self());
        } else {
            simulation.tell(new Simulation.Register(initialIndex), self());
        }
    }

    /**
     * To be implemented by concrete UntypedActor, this defines the behavior of the
     * UntypedActor.
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Simulation.SimulationUpdate) {
            log.debug("Received a SimulationUpdate! Processing...");
            currentState = ((Simulation.SimulationUpdate) message).currentState();
            sender().tell(onUpdate((Simulation.SimulationUpdate) message), self());
        } else {
            unhandled(message);
        }
    }

    public void spawnChild(final Class<? extends AmcgalaAgent> childClass, final World.Index index) {
        Props props = Props.create(new AmcgalaAgentCreator(childClass, index));
        context().actorOf(props);
    }

    abstract public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update);

    static class AmcgalaAgentCreator implements Creator<Actor> {
        Class<? extends AmcgalaAgent> childClass;
        World.Index index;

        AmcgalaAgentCreator(Class<? extends AmcgalaAgent> childClass, World.Index index) {
            this.childClass = childClass;
            this.index = index;
        }

        public Actor create() throws Exception {
            return childClass.getConstructor(World.Index.class).newInstance(index);
        }

    }
}


