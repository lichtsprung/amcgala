package org.amcgala.example.lsystem;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.camera.OrthographicCamera;
import org.amcgala.framework.math.Vector3d;
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

        LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(
                new Axiom("+F"),
                new Rules()
                        .addReplacementRule("F", "F-F+F+F-F")
                        .addDrawingRule("F", "M"),
                new Level(5),
                new Length(600 / Math.pow(3, 5)),
                new Angle(60),
                scene
        );
        // TODO Skalierung und Translation der Szene durch Anpassen der maximalen Ausmaße
        lindenmayerSystem.run();
        framework.addScene(scene);
        framework.start();
    }

    public static void main(String[] args) {
        new LSystem();
    }
}
