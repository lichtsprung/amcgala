package org.amcgala.example.basic;

import com.google.common.eventbus.Subscribe;
import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.event.KeyPressedEvent;
import org.amcgala.framework.lighting.AmbientLight;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.Polygon;
import org.amcgala.framework.shape.util.PLYPolygonParser;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Hier ist unser tolles evolutionäres Programm, das vielleicht irgendwann funktionieren wird.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Basic extends Amcgala {

    public Basic() throws Exception {
        final Scene scene = new Scene("one");
        final Node node = new Node("lineNode");
        Line line = new Line(-100, -100, 100, 100, "Line1");

        scene.add(line, node);

        // Hinzufügen eines Szenen-InputHandlers
        scene.addInputHandler(new InputHandler() {
            Random random = new Random(System.currentTimeMillis());

            @Subscribe
            public void addRandomLine(KeyPressedEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    scene.add(new Line(-400 + random.nextInt(800),
                                       -300 + random.nextInt(600),
                                       -400 + random.nextInt(800),
                                       -300 + random.nextInt(600)), node);
                }
            }
        }, "Random Line");

        framework.addScene(scene);

        Scene scene2 = new Scene("two");
        Node node2 = new Node("lineNode");
        Node node3 = new Node("testNode");
        Line line2 = new Line(-100, 100, 100, 100, "Line2");
        Line line3 = new Line(-100, -100, 100, -100, "Line3");


        // Hinzufügen von Baumhierarchien
        scene2.addNode(node2);  // root -> node2
        scene2.addNode(node3, node2); // root -> node2 -> node3

        // Hinzufügen eines Shapes zu einem bestimmten Knoten über das Label des Knotens
        scene2.addShape(line2, "lineNode");
        scene2.addShape(line3, "lineNode");

        framework.addScene(scene2);

        Scene scene3 = new Scene("three");
        for (Polygon p : PLYPolygonParser.parseAsPolygonList("src/main/resources/org/amcgala/example/plyparser/monkey" +
                                                                     ".ply", 300)) {
            scene3.addShape(p);
        }

        // TODO Bei Verwendung des Lichts wird eine NaN Exception geworfen.
        //scene3.addLight(new AmbientLight("ambient", 1, Color.RED));
        framework.addScene(scene3);

        // Hinzufügen eines Framework-InputHandlers
        framework.addInputHandler(new InputHandler() {
            @Subscribe
            public void switchScenes(KeyPressedEvent e) {
                if (KeyEvent.VK_SPACE == e.getKeyCode()) {
                    framework.nextScene();
                }
            }
        }, "Switch Scene");

        framework.start();
    }

    public static void main(String[] args) {
        try{
            new Basic();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
