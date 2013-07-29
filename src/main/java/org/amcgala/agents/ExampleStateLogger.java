package org.amcgala.agents;

import akka.actor.Actor;
import akka.japi.Creator;
import org.amcgala.Scene;
import org.amcgala.shape.Point;

import java.awt.*;
import java.util.Map;

/**
 *
 */
public class ExampleStateLogger extends StateLoggerAgent {
    Scene scene = new Scene("line");


    public ExampleStateLogger() {
        framework.addScene(scene);
    }


    @Override
    public void onUpdate(Map<World.Index, World.Cell> cells, Map<Integer, Agent.AgentState> agents) {
        scene.removeShapes();
        for (Agent.AgentState s : agents.values()) {
            Point p = new Point(s.position().x(), s.position().y(), -1);
            p.setColor(Color.RED);
            scene.addShape(p);
        }
    }

    static class StateLoggerCreator implements Creator<Actor> {

        @Override
        public Actor create() throws Exception {
            return new ExampleStateLogger();
        }
    }
}
