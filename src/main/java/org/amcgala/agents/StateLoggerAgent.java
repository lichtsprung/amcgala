package org.amcgala.agents;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.amcgala.Framework;
import org.amcgala.FrameworkMode;

import java.util.HashSet;
import java.util.Set;

/**
 * A StateLoggerAgent receives a SimulationUpdate every time something changes in the simulated world. It provides all
 * necessary information that is needed for a visualisation of the simulation.
 */
public abstract class StateLoggerAgent extends UntypedActor {
    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    protected final Framework framework = Framework.getInstance(FrameworkMode.SOFTWARE);

    private HashSet<World.CellWithIndex> cells = new HashSet<>();
    private HashSet<Agent.AgentState> agents = new HashSet<>();


    // TODO Shouldn't be hardcoded!
    private ActorSelection simulation = getContext().system().actorSelection("akka.tcp://Simulator@localhost:2552/user/simulation");


    /**
     * User overridable callback.
     * <p/>
     * Is called when an Actor is started.
     * Actors are automatically started asynchronously when created.
     */
    @Override
    public void preStart() throws Exception {
        simulation.tell(Simulation.RegisterStateLogger$.MODULE$, self());
    }

    /**
     * To be implemented by concrete UntypedActor, this defines the behavior of the
     * UntypedActor.
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Simulation.SimulationState) {
            Simulation.SimulationState state = (Simulation.SimulationState) message;
            log.info("new state!");

            agents.addAll(state.agents());
            cells.addAll(state.worldInfo().cells());

            log.info("Init finished");

            onUpdate(cells, agents);
        } else if (message instanceof Simulation.SimulationStateUpdate) {
            log.info("Update...");
            Simulation.SimulationStateUpdate state = (Simulation.SimulationStateUpdate) message;
            agents.addAll(state.agents());
            cells.addAll(state.changedCells());

            onUpdate(cells, agents);
        } else {
            unhandled(message);
        }
    }

    abstract public void onUpdate(Set<World.CellWithIndex> cells, Set<Agent.AgentState> agents);
}
