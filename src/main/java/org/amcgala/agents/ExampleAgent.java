package org.amcgala.agents;

import java.util.Random;

/**
 * Example Agent. Walks randomly across the field.
 */
public class ExampleAgent extends AmcgalaAgent {
    private boolean step = false;

    @Override
    public Agent.AgentMessage onUpdate(Simulation.SimulationUpdate update) {

        Random r = new Random(System.nanoTime());
        if (step) {
            step = !step;
            World.CellWithIndex cell = update.neighbours()[r.nextInt(update.neighbours().length)];
            return new Agent.MoveTo(cell);
        } else {
            step = !step;
            return new Agent.ChangeValue(r.nextDouble());
        }
    }
}
