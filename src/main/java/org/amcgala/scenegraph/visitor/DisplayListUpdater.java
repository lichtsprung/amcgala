package org.amcgala.scenegraph.visitor;

import org.amcgala.math.Matrix;
import org.amcgala.renderer.DisplayList;
import org.amcgala.scenegraph.Node;
import org.amcgala.scenegraph.transform.Transformation;
import org.amcgala.shape.Shape;

/**
 * Visitor, der den Szenengraph traversiert und dabei die DisplayList des Frameworks aktualisiert. Der DisplayListUpdater
 * berücksichtigt dabei auch die Transformationen, die innerhalb des Szenengraphs gefunden werden.
 */
public class DisplayListUpdater implements Visitor{
    private DisplayList list;

    public DisplayListUpdater(DisplayList list){
        this.list = list;
    }

    @Override
    public void visit(Node node) {
        Matrix transform = node.getTransformMatrix();
        for (Shape s : node.getShapes()) {
            list.addAll(s.getDisplayList(list, new Transformation() {
                @Override
                public Matrix getTransformMatrix() {
                    return transform;
                }

                @Override
                public void update() {

                }
            }));
        }
    }
}
