package org.amcgala.agents;

import org.amcgala.Scene;
import org.amcgala.shape.Point;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/29/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleStateLogger extends StateLoggerAgent {

    private Scene scene = new Scene("Vizz");


    public ExampleStateLogger() {
        super();
        framework.addScene(scene);
    }

    @Override
    public void onUpdate(Simulation.SimulationState update) {
        if (update.agents().size() > 0) {
            World.Index idx = update.agents().head().position().index();
            scene.addShape(new Point(idx.x(), idx.y(), -1));
        }
    }
}
