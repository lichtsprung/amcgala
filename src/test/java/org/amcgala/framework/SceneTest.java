package org.amcgala.framework;

import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.Shape;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Funktionalität einer {@link Scene}.
 */
public class SceneTest {
    private Scene scene;

    @Before
    public void init(){
        scene = new Scene();
    }

    @Test
    public void addShapeToScene() {
        scene.add(new Line(0, 0, 100, 100), "Line1");
        Shape s = scene.getShape("Line1");

    }
}
