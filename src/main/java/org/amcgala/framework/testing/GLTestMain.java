package org.amcgala.framework.testing;


import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.Point;
import org.amcgala.framework.shape.util.CompositeShape;
import org.amcgala.framework.shape.util.lsystem.*;

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
            scene.addShape(new Point(r.nextFloat() * 800, r.nextFloat() * 600, -1));
        }
        CompositeShape shape = new CompositeShape();

        LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(
                new Axiom("X"),
                new Rules()
                        .addReplacementRule("F", "FF")
                        .addReplacementRule("X", "F-[X+X]-X")
                        .addDrawingRule("F", "M")
                        .addDrawingRule("X", ""),
                new Level(6),
                new Length(6),
                new Angle(22),
                shape,
                new Vector3d(100, 400, -1),
                90
        );

        lindenmayerSystem.run();

        scene.addShape(shape);
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
