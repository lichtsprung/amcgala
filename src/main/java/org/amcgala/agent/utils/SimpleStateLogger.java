package org.amcgala.agent.utils;

import akka.actor.Actor;
import akka.japi.Creator;
import org.amcgala.RGBColor;
import org.amcgala.Scene;
import org.amcgala.agent.Agent;
import org.amcgala.agent.StateLoggerAgent;
import org.amcgala.agent.World;
import org.amcgala.math.Vertex3f;
import org.amcgala.shape.Rectangle;

import java.util.Map;

/**
 *
 */
public final class SimpleStateLogger extends StateLoggerAgent {
    Scene scene = new Scene("SimpleStateLogger");
    Rectangle[][] rectangles;
    RGBColor[] greys;


    @Override
    public void onInit() {
        greys = new RGBColor[1000];
        float step = 1f / greys.length;

        for (int i = 0; i < greys.length; i++) {
            greys[i] = new RGBColor(i * step, i * step, i * step);
        }

        rectangles = new Rectangle[worldWidth][worldHeight];

        for (int x = 0; x < rectangles.length; x++) {
            for (int y = 0; y < rectangles[0].length; y++) {
                rectangles[x][y] = new Rectangle(new Vertex3f(x * scaleX, y * scaleY, -1), (float) scaleX, (float) scaleY);
                rectangles[x][y].setColor(RGBColor.GREEN);
                scene.addShape(rectangles[x][y]);
            }
        }


        framework.addScene(scene);
    }

    @Override
    public void onUpdate(Map<World.Index, World.Cell> cells, Map<Agent.AgentID, Agent.AgentState> agents) {

//        RGBColor pheromoneColor = new RGBColor(0, 0, 0);
//        RGBColor white = new RGBColor(1f, 1f, 1f);
//            if (e.getValue().pheromones().size() > 0) {
//                float strength = (float) e.getValue().pheromones().head()._2();
//                p.setColor(white.times(1f - strength));
//            } else {
//                p.setColor(white);
//            }

        for (Map.Entry<World.Index, World.Cell> entry : cells.entrySet()) {
            Rectangle p = rectangles[entry.getKey().x()][entry.getKey().y()];
            int index = Math.max(Math.min(Math.round(Math.min(entry.getValue().value(), 1f) * greys.length), greys.length - 1),0);

            p.setColor(greys[index]);

        }

        for (Agent.AgentState s : agents.values()) {
            Rectangle p = rectangles[s.position().x()][s.position().y()];
            p.setColor(RGBColor.BLUE);
        }
    }

    static class StateLoggerCreator implements Creator<Actor> {
        @Override
        public Actor create() throws Exception {
            return new SimpleStateLogger();
        }
    }
}
