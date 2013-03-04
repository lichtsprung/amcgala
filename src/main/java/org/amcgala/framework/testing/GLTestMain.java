package org.amcgala.framework.testing;


import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.scenegraph.transform.RotationZ;
import org.amcgala.framework.shape.Line;

import java.awt.*;
import java.util.Random;

/**
 * Testklasse für die GL Funktionalität.
 */
public class GLTestMain extends Amcgala {


    public GLTestMain() {
        Scene scene = new Scene("line");
//        Random r = new Random(System.nanoTime());
//        for (int i = 0; i < r.nextInt(1000); i++) {
//            Line line = new Line(new Vertex3f((float) r.nextInt(800), (float) r.nextInt(600), 0), new Vertex3f((float) r.nextInt(800), (float) r.nextInt(600), 0));
//            line.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//            scene.addShape(line);
//        }
//        scene.addTransformation(new RotationZ());
        Line line1 = new Line(new Vertex3f(100, 100, 0), new Vertex3f(200, 100, 0));
        Line line2 = new Line(new Vertex3f(100, 300, 0), new Vertex3f(200, 300, 0));
        scene.addShape(line1);
        scene.addShape(line2);
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
