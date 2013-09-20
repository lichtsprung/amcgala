package org.amcgala.testing.agent;

import org.amcgala.agent.Agent;
import org.amcgala.agent.AmcgalaAgent;
import org.amcgala.agent.Simulation;
import org.amcgala.agent.World;

/**
 * Fuellt ein Polygon mit einem Flood Fill Algorithmus.
 */
public class FloodFillAgent extends AmcgalaAgent {

    private boolean changed = false;

    @Override
    protected Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {
        if (update.currentState().cell().value() == 0) {
            changed = true;
            return new Agent.ChangeValue(1);
        } else if (update.currentState().cell().value() > 0 && !changed) {
            return die();
        } else {
            for (World.CellWithIndex neighbour : update.neighbours()) {
                spawnChild(neighbour.index());
            }
            return die();
        }
    }
}

