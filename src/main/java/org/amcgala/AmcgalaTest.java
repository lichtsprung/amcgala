package org.amcgala;

import org.amcgala.animation.interpolation.EaseOutInterpolation;
import org.amcgala.animation.interpolation.LinearInterpolation;
import org.amcgala.math.Vertex3f;
import org.amcgala.scenegraph.transform.RotationZ;
import org.amcgala.scenegraph.transform.Translation;
import org.amcgala.shape.Line;

/**
 * Created by rgiacinto on 04/08/14.
 */
public class AmcgalaTest extends Amcgala {
    public AmcgalaTest() {
        Line line = new Line(new Vertex3f(0, 0, 1), new Vertex3f(200, 200, 1));
        Scene scene = new Scene("line");

        Translation trans = new Translation(100, 100, 0);
        trans.setInterpolationX(new LinearInterpolation(100, 200,300,true));

        RotationZ rot = new RotationZ();
        rot.setInterpolationPhi(new LinearInterpolation(0, 2 * Math.PI, 400, true));

        scene.addTransformation(trans);
        scene.addTransformation(rot);

        scene.addShape(line);
        framework.addScene(scene);
        framework.loadScene("line");
    }

    public static void main(String[] args) {
        new AmcgalaTest();
    }
}
