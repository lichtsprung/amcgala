package org.amcgala.framework.testing;


import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.shape.Line;

/**
 * Testklasse für die GL Funktionalität.
 */
public class GLTestMain extends Amcgala {


    public GLTestMain() {
        Scene scene = new Scene("line");
        scene.addShape(new Line(
                new Vertex3f(0, 0, 0),
                new Vertex3f(100, 100, 0)
        ));
        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new GLTestMain();
    }
}
