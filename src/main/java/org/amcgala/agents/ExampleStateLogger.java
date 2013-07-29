package org.amcgala.agents;

import org.amcgala.Scene;
import org.amcgala.shape.Point;

import java.awt.*;
import java.util.Set;

/**
 *
 */
public class ExampleStateLogger extends StateLoggerAgent {
    Scene scene = new Scene("line");


    public ExampleStateLogger() {
        framework.addScene(scene);
    }


    @Override
    public void onUpdate(Set<World.CellWithIndex> cells, Set<Agent.AgentState> agents) {
        scene.removeShapes();
        log.info("Start agents");
        for (Agent.AgentState s : agents) {
            Point p = new Point(s.position().index().x(), s.position().index().y(), -1);
            p.setColor(Color.RED);
            scene.addShape(p);
        }
        log.info("end agents");
    }
}
