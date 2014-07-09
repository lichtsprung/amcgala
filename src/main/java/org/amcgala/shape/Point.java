package org.amcgala.shape;

import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.scenegraph.transform.Transformation;
import org.amcgala.shape.primitives.PointPrimitive;

/**
 * Ein Punkt.
 */
public class Point extends AbstractShape {
    private PointPrimitive p;


    public Point(float x, float y, float z) {
        p = new PointPrimitive(new Vertex3f(x, y, z));
    }

    @Override
    public DisplayList getDisplayList(DisplayList list) {
        p.color = getColor();
        list.points.add(p);

        return list;
    }

    @Override
    public DisplayList getDisplayList(DisplayList list, Transformation transformation) {
        // TODO Füge die Primitiven der DisplayList hinzu, aber vorher transformiere die Vektoren mithilfe des Transformationsobjekts.
        return super.getDisplayList(list, transformation);
    }
}
