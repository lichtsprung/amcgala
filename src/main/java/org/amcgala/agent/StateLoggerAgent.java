package org.amcgala.agent;

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
    private Map<Agent.AgentID, Agent.AgentState> agents = new HashMap<>();

    protected int worldWidth;
    protected int worldHeight;
    protected int scaleX;
    protected int scaleY;

    private final String simulationPath = getContext().system().settings().config().getString("org.amcgala.agent.simulation.address");
    private final ActorSelection simulation = getContext().actorSelection(simulationPath);


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
            log.info("Got State!");
            worldWidth = state.worldInfo().width();
            worldHeight = state.worldInfo().height();
            scaleX = framework.getWidth() / worldWidth;
            scaleY = framework.getHeight() / worldHeight;

            for (Tuple2<World.Index, World.Cell> entry : state.worldInfo().cells()) {
                cells.put(entry._1(), entry._2());
            }

            for (Agent.AgentState as : state.agents()) {
                agents.put(as.id(), as);
            }

            onInit();
            onUpdate(cells, agents);
        } else if (message instanceof Simulation.SimulationStateUpdate) {

            Simulation.SimulationStateUpdate state = (Simulation.SimulationStateUpdate) message;

            agents.clear();
            for (Agent.AgentState as : state.agents()) {
                agents.put(as.id(), as);
            }

            cells.clear();
            for (Tuple2<World.Index, World.Cell> entry : state.changedCells()) {
                cells.put(entry._1(), entry._2());
            }

            onUpdate(cells, agents);
        } else {
            unhandled(message);
        }
    }


    abstract public void onInit();

    abstract public void onUpdate(Map<World.Index, World.Cell> cells, Map<Agent.AgentID, Agent.AgentState> agents);
}
