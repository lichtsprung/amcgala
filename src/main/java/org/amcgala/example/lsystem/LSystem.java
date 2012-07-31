package org.amcgala.example.lsystem;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.camera.OrthographicCamera;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.scenegraph.transform.Translation;
import org.amcgala.framework.shape.util.CompositeShape;
import org.amcgala.framework.shape.util.lsystem.Angle;
import org.amcgala.framework.shape.util.lsystem.Axiom;
import org.amcgala.framework.shape.util.lsystem.Length;
import org.amcgala.framework.shape.util.lsystem.Level;
import org.amcgala.framework.shape.util.lsystem.LindenmayerSystem;
import org.amcgala.framework.shape.util.lsystem.Rules;

/**
 * Beispiel für die Benutzung eines {@link org.amcgala.framework.shape.util.lsystem.LindenmayerSystem}.
 *
 * @author Robert Giacinto
 */
public class LSystem extends Amcgala {

    public LSystem() {
        Scene scene = new Scene("lsystem");
        scene.setCamera(new OrthographicCamera(Vector3d.UNIT_Y, new Vector3d(0, 0, 1), new Vector3d(0, 0, -1)));
        CompositeShape shape = new CompositeShape();
        scene.add(shape);

        LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(
                new Axiom("X"),
                new Rules()
                        .addReplacementRule("F", "FF")
                        .addReplacementRule("X", "F-[[X]+X]+F[+FX]-X")
                        .addDrawingRule("F", "M")
                        .addDrawingRule("X", ""),
                new Level(5),
                new Length(5),
                new Angle(22.3),
                shape
        );
        // TODO Skalierung und Translation der Szene durch Anpassen der maximalen Ausmaße
        lindenmayerSystem.run();

        if (shape.getBoundingBox().getWidth() < framework.getWidth() && shape.getBoundingBox().getHeight() < framework.getHeight()) {
            scene.add(new Translation(-shape.getBoundingBox().getCenter().x, -shape.getBoundingBox().getCenter().y, -shape.getBoundingBox().getCenter().z));
        }

        framework.addScene(scene);
        framework.start();
    }

    public static void main(String[] args) {
        new LSystem();
    }
}
