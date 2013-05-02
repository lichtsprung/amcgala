package org.amcgala.framework.testing;


import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.Scene;
import org.amcgala.framework.shape.Point;

import java.util.Random;

/**
 * Testklasse für die GL Funktionalität.
 */
public class GLTestMain {
    final Framework framework = Framework.getInstance(FrameworkMode.SOFTWARE);

    public GLTestMain() {
        Scene scene = new Scene("line");
        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < 1000; i++) {
            scene.add(new Point(r.nextFloat() * 800, r.nextFloat() * 600, -1));
        }
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
