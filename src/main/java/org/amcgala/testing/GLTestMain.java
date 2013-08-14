package org.amcgala.testing;


import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.RGBColor;
import org.amcgala.Scene;
import org.amcgala.math.Vector3d;
import org.amcgala.shape.util.CompositeShape;
import org.amcgala.shape.util.lsystem.*;

/**
 * Testklasse für die GL Funktionalität.
 */
public class GLTestMain {
    final Framework framework = Framework.getInstance(FrameworkMode.SOFTWARE);

    public GLTestMain() {
        Scene scene = new Scene("line");

        CompositeShape shape = new CompositeShape();
        shape.setColor(RGBColor.BLUE);

        LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(
                new Axiom("X"),
                new Rules()
                        .addReplacementRule("F", "FF")
                        .addReplacementRule("X", "F-[[X]+X]+F[+FX]-X")
                        .addDrawingRule("F", "M")
                        .addDrawingRule("X", ""),
                new Level(6),
                new Length(3),
                new Angle(25),
                shape,
                new Vector3d(80, 500, -1),
                45
        );

        lindenmayerSystem.run();

        scene.addShape(shape);


        CompositeShape shape2 = new CompositeShape();
        shape2.setColor(RGBColor.RED);

        LindenmayerSystem lindenmayerSystem2 = new LindenmayerSystem(
                new Axiom("FX"),
                new Rules()
                        .addReplacementRule("Y", "FX-Y")
                        .addReplacementRule("X", "X+YF")
                        .addDrawingRule("F", "M")
                        .addDrawingRule("Y", "")
                        .addDrawingRule("X", ""),
                new Level(10),
                new Length(15),
                new Angle(90),
                shape2,
                new Vector3d(500, 400, -1),
                0
        );

        lindenmayerSystem2.run();
        scene.addShape(shape2);
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
