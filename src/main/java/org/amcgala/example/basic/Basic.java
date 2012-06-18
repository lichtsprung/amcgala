package org.amcgala.example.basic;

import org.amcgala.Framework;
import org.amcgala.framework.Scene;
import org.amcgala.framework.shape.Line;

import java.awt.Frame;

/**
 * Hier ist unser tolles evolutionäres Programm, das vielleicht irgendwann funktionieren wird.
 * @author Robert Giacinto
 * @since 2.0
 */
public class Basic {


    public static void main(String[] args) {
        Framework framework = new Framework(800, 600);

        Scene scene = new Scene("Basic");
        scene.add(new Line(0, 0, 100, 100));

        framework.addScene(scene);

        framework.setFPS(60);
        framework.start();
    }
}
