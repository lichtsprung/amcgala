package org.amcgala.testing;

import org.amcgala.Amcgala;
import org.amcgala.RGBColor;
import org.amcgala.Scene;
import org.amcgala.shape.Point;

import java.util.Random;

/**
 * Test zu Profilingzwecken.
 */
public class HeavyLoadTest extends Amcgala {
    public HeavyLoadTest() {
        Scene scene = new Scene("HeavyLoad");
        Random r = new Random(System.nanoTime());
        for (int i = 0; i < 1000000; i++) {
            Point p = new Point(r.nextInt(800), r.nextInt(600), 0);
            p.setColor(new RGBColor(r.nextFloat(), r.nextFloat(), r.nextFloat()));
            scene.addShape(p);
        }
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new HeavyLoadTest();
    }
}
