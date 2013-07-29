package org.amcgala.agents;

import java.util.Random;

/**
 * Example Agent. Walks randomly across the field.
 */
public class ExampleAgent extends AmcgalaAgent {
    private Random r = new Random(System.nanoTime());


    public ExampleAgent(World.Index initialIndex) {
        super(initialIndex);
    }

    @Override
    public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {
        if (r.nextBoolean()) {
            if (r.nextDouble() < 0.01) {
                log.info("Spawning child at {}", currentState.position());
                spawnChild(this.getClass(), currentState.position());
            }
            World.CellWithIndex cell = update.neighbours()[r.nextInt(update.neighbours().length)];
            return new Agent.MoveTo(cell.index());
        } else {
            return new Agent.ChangeValue(r.nextDouble());
        }

    }
}
