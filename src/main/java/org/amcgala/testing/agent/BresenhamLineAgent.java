package org.amcgala.testing.agent;

import org.amcgala.agent.Agent;
import org.amcgala.agent.AmcgalaAgent;
import org.amcgala.agent.Simulation;
import org.amcgala.agent.World;


/**
 * Zeichnet eine Line von A nach B.
 */
public class BresenhamLineAgent extends AmcgalaAgent {
    private int sx;
    private int sy;
    private int ex;
    private int ey;

    public BresenhamLineAgent(int sx, int sy, int ex, int ey) {
        getSelf().tell(new Agent.SpawnAt(new World.Index(sx, sy)), getSelf());
    }

    @Override
    protected Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
