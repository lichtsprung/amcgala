package org.amcgala.testing.agent;

import org.amcgala.agent.Agent;
import org.amcgala.agent.AmcgalaAgent;
import org.amcgala.agent.Simulation;
import org.amcgala.agent.World;

import java.util.Random;

/**
 * Example Agent. Walks randomly across the field.
 */
public class ExampleAgent extends AmcgalaAgent {
    private Random r = new Random(System.nanoTime());


    public ExampleAgent(World.Index startPosition) {
        super(startPosition);
    }

    @Override
    public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {
        if (r.nextBoolean()) {

            if (r.nextDouble() < 0.01) {
                spawnChild(this.getClass(), currentState.position());
            }
            World.CellWithIndex cell = update.neighbours()[r.nextInt(update.neighbours().length)];
            return new Agent.MoveTo(cell.index());
        } else {
            return new Agent.ChangeValue(r.nextDouble());
        }

    }
}
