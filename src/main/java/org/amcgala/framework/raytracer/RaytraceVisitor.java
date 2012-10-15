package org.amcgala.framework.raytracer;

import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.visitor.Visitor;

/**
 * Der RaytraceVisitor traversiert den {@link org.amcgala.framework.scenegraph.SceneGraph} und berechnet die Schnittpunkte
 * den Objekten innerhalb der Szene.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RaytraceVisitor implements Visitor{

    @Override
    public void visit(Node node) {
        synchronized (node.getShapes()) {

        }
    }
}
