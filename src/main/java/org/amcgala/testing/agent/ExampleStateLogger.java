package org.amcgala.testing.agent;

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
public final class ExampleStateLogger extends StateLoggerAgent {
    Scene scene = new Scene("ExampleStateLogger");
    Rectangle[][] points;


    @Override
    public void onInit() {
        points = new Rectangle[worldWidth][worldHeight];

        for (int x = 0; x < points.length; x++) {
            for (int y = 0; y < points[0].length; y++) {
                points[x][y] = new Rectangle(new Vertex3f(x * scaleX, y * scaleY, -1), (float) scaleX, (float) scaleY);
                points[x][y].setColor(RGBColor.GREEN);
                scene.addShape(points[x][y]);
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
            Rectangle p = points[entry.getKey().x()][entry.getKey().y()];
            float c = 1f - (float) entry.getValue().value();
            RGBColor valueColor = new RGBColor(c, c, c);
            p.setColor(valueColor);

        }

        for (Agent.AgentState s : agents.values()) {
            Rectangle p = points[s.position().x()][s.position().y()];
            p.setColor(RGBColor.RED);
        }
    }

    static class StateLoggerCreator implements Creator<Actor> {
        @Override
        public Actor create() throws Exception {
            return new ExampleStateLogger();
        }
    }
}
