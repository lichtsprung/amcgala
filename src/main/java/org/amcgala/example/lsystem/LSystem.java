package org.amcgala.example.lsystem;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.camera.OrthographicCamera;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.scenegraph.transform.Scale;
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
        final Scene scene = new Scene("lsystem");
        scene.setCamera(new OrthographicCamera(Vector3d.UNIT_Y, new Vector3d(0, 0, 1), new Vector3d(0, 0, -1)));
        CompositeShape shape = new CompositeShape();
        scene.addShape(shape);

        LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(
                new Axiom("X"),
                new Rules()
                        .addReplacementRule("F", "FF")
                        .addReplacementRule("X", "F-[[X]+X]+F[+FX]-X")
                        .addDrawingRule("F", "M")
                        .addDrawingRule("X", ""),
                new Level(6),
                new Length(5),
                new Angle(22.3),
                shape
        );

        lindenmayerSystem.run();

        final Translation translation = new Translation(0, 0, 0);
        final Scale scaling = new Scale(1, 1, 1);
        scene.addTransformation(translation);
        scene.addTransformation(scaling);

        shape.setAnimation(new Animation<CompositeShape>(shape) {

            @Override
            public void update() {

                if (shape.getBoundingBox().getWidth() < framework.getWidth() && shape.getBoundingBox().getHeight() < framework.getHeight()) {
                    double x = shape.getBoundingBox().getCenter().x;
                    double y = shape.getBoundingBox().getCenter().y;
                    double width = shape.getBoundingBox().getWidth();
                    double height = shape.getBoundingBox().getHeight();

                    if (x + width / 2 > framework.getWidth() / 2) {
                        double diffX = (x + width / 2) - (framework.getWidth() / 2);
                        translation.changeX(-diffX);
                    }
                    if (y + height / 2 > (framework.getHeight() / 2)) {
                        double diffY = (y + height / 2) - (framework.getHeight() / 2);
                        translation.changeY(-diffY);
                    }
                } else {
                    double scaleFactor;
                    if (shape.getBoundingBox().getWidth() >= framework.getWidth() &&
                            shape.getBoundingBox().getHeight() >= framework.getHeight()) {
                        scaleFactor = Math.max(
                                shape.getBoundingBox().getWidth() / framework.getWidth(),
                                shape.getBoundingBox().getHeight() / framework.getHeight());
                    } else if (shape.getBoundingBox().getWidth() > framework.getWidth()) {
                        scaleFactor = shape.getBoundingBox().getWidth() / framework.getWidth();
                    } else {
                        scaleFactor = shape.getBoundingBox().getHeight() / framework.getHeight();
                    }
                    scaling.setScaleX(1 / scaleFactor);
                    scaling.setScaleY(1 / scaleFactor);
                    scaling.setScaleZ(1 / scaleFactor);
                }
            }
        });

        framework.addScene(scene);
        framework.start();
    }

    public static void main(String[] args) {
        new LSystem();
    }
}
