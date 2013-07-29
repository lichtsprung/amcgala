package org.amcgala.agents;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * An Agent
 * <ul>
 * <li>receives its currentState vicinity in every time step</li>
 * </ul>
 */
public abstract class AmcgalaAgent extends UntypedActor {

    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected Agent.AgentState currentState;

    // TODO Shouldn't be hardcoded!
    private ActorSelection simulation = getContext().system().actorSelection("akka.tcp://Simulator@localhost:2552/user/simulation");


    /**
     * User overridable callback.
     * <p/>
     * Is called when an Actor is started.
     * Actor are automatically started asynchronously when created.
     * Empty default implementation.
     */
    @Override
    public void preStart() throws Exception {
        simulation.tell(Simulation.Register$.MODULE$, self());
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

    abstract public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update);
}
