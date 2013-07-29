package org.amcgala.agents;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

/**
 * A StateLoggerAgent receives a SimulationUpdate every time something changes in the simulated world. It provides all
 * necessary information that is needed for a visualisation of the simulation.
 */
public abstract class StateLoggerAgent extends UntypedActor {
    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    protected final Framework framework = Framework.getInstance(FrameworkMode.SOFTWARE);

    private Map<World.Index, World.Cell> cells = new HashMap<>();
    private Map<Integer, Agent.AgentState> agents = new HashMap<>();

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


            for (Tuple2<World.Index, World.Cell> entry : state.worldInfo().cells()) {
                cells.put(entry._1(), entry._2());
            }

            for (Agent.AgentState as : state.agents()) {
                agents.put(as.id(), as);
            }

            log.info("Init finished");

            onUpdate(cells, agents);
        } else if (message instanceof Simulation.SimulationStateUpdate) {
            log.info("Update...");
            Simulation.SimulationStateUpdate state = (Simulation.SimulationStateUpdate) message;
            for (Agent.AgentState as : state.agents()) {
                agents.put(as.id(), as);
            }
            for (Tuple2<World.Index, World.Cell> entry : state.changedCells()) {
                cells.put(entry._1(), entry._2());
            }

            onUpdate(cells, agents);
        } else {
            unhandled(message);
        }
    }

    abstract public void onUpdate(Map<World.Index, World.Cell> cells, Map<Integer, Agent.AgentState> agents);
}
