package org.amcgala.framework.testing;


import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.Triangle;

/**
 * Testklasse für die GL Funktionalität.
 */
public class GLTestMain {
    final Framework framework = Framework.getInstance(FrameworkMode.GL);

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
        Triangle triangle = new Triangle(new Vertex3f(100, 100, 0), new Vertex3f(300, 100, 0), new Vertex3f(200, 50, 0));
//        scene.addShape(line1);
//        scene.addShape(line2);
        scene.addShape(triangle);
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
