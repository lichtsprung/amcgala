package org.amcgala.testing;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.shape.Point;

/**
 * Test zu Profilingzwecken.
 */
public class HeavyLoadTest extends Amcgala {
    public HeavyLoadTest() {
        Scene scene = new Scene("HeavyLoad");
        for (int i = 0; i < 500000; i++) {
            scene.addShape(new Point(0, 0, 0));
            if (i % 1000 == 0) {
                System.out.println(i / 500000.0);
            }
        }
    }

    public static void main(String[] args) {
        new HeavyLoadTest();
    }
}
