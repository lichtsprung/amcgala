package org.amcgala.example.transformations;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.transform.RotationZ;
import org.amcgala.framework.scenegraph.transform.Translation;
import org.amcgala.framework.shape.Line;

/**
 * Beispiel, das die neue Transformationsfunktionalität austestet.
 *
 * @author Robert Giacinto
 */
public class TransformationExample extends Amcgala {

    public TransformationExample() {
        Scene scene = new Scene("transformation scene");
        Line line = new Line(0, 0, 200, 0);
        Node node = new Node("linie");
        scene.add(node);
        scene.add(line, node);

        scene.add(new Translation(100, 0, 0));
        node.add(new RotationZ(Math.PI / 17));

        framework.add(scene);
        framework.start();
    }

    public static void main(String[] args) {
        new TransformationExample();
    }
}
