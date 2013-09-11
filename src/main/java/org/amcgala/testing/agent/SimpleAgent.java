package org.amcgala.testing.agent;

import org.amcgala.agent.Agent;
import org.amcgala.agent.AmcgalaAgent;
import org.amcgala.agent.Simulation;
import org.amcgala.agent.World;

import java.util.Random;

/**
 * Example Agent. Walks randomly across the field.
 */
public class SimpleAgent extends AmcgalaAgent {
    private Random r = new Random(System.nanoTime());


    @Override
    public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {
        if (r.nextFloat() < 0.05f) {
            spawnChild();
            World.CellWithIndex cell = update.neighbours()[r.nextInt(update.neighbours().length)];
            return new Agent.MoveTo(cell.index());
        } else if (r.nextFloat() >= 0.05f && r.nextFloat() < 0.8f) {
            World.CellWithIndex cell = update.neighbours()[r.nextInt(update.neighbours().length)];
            return new Agent.MoveTo(cell.index());
        } else if (r.nextFloat() >= 0.8f && r.nextFloat() < 0.99f) {
            return die();
        } else {
            return new Agent.ChangeValue(r.nextFloat());
        }
    }
}
