package org.amcgala.testing.agent;

import akka.actor.Actor;
import akka.japi.Creator;
import org.amcgala.Scene;
import org.amcgala.agent.Agent;
import org.amcgala.agent.StateLoggerAgent;
import org.amcgala.agent.World;
import org.amcgala.shape.Point;

import java.awt.*;
import java.util.Map;

/**
 *
 */
public final class ExampleStateLogger extends StateLoggerAgent {
    Scene scene = new Scene("ExampleStateLogger");
    Point[][] points;


    @Override
    public void onInit() {
        log.info("width: {} ", worldWidth);
        log.info(("height: {}"), worldHeight);
        log.info("scaleX: {}", scaleX);
        log.info("scaleY: {}", scaleY);
        points = new Point[worldWidth][worldHeight];
        int counter = 0;

        for (int x = 0; x < points.length; x++) {
            for (int y = 0; y < points[0].length; y++) {
                points[x][y] = new Point(x * scaleX, y * scaleY, -1);
                points[x][y].setColor(Color.GREEN);
                // TODO AddShape ist VIEL ZU langsam!
                scene.addShape(points[x][y]);
                counter++;
            }
            log.info("{} % initialisiert", (counter / ((double) worldWidth * worldHeight)) * 100);
        }
        framework.addScene(scene);
        log.info("Scene added");
    }

    @Override
    public void onUpdate(Map<World.Index, World.Cell> cells, Map<Integer, Agent.AgentState> agents) {
        for (Map.Entry<World.Index, World.Cell> e : cells.entrySet()) {
            Point p = points[e.getKey().x()][e.getKey().y()];
            float c = 1f - (float) e.getValue().value();
            p.setColor(new Color(c, c, c));
        }
        for (Agent.AgentState s : agents.values()) {
            Point p = points[s.position().x()][s.position().y()];
            p.setColor(Color.RED);
        }

    }

    static class StateLoggerCreator implements Creator<Actor> {

        @Override
        public Actor create() throws Exception {
            return new ExampleStateLogger();
        }
    }
}
