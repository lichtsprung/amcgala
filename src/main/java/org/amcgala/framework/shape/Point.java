package org.amcgala.framework.shape;

import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.renderer.DisplayList;
import org.amcgala.framework.shape.primitives.PointPrimitive;

/**
 * Ein Punkt.
 */
public class Point extends AbstractShape {
    private PointPrimitive p;


    public Point(float x, float y, float z) {
        p = new PointPrimitive(new Vertex3f(x, y, z));
    }

    @Override
    public DisplayList getDisplayList() {
        DisplayList list = new DisplayList();
        p.color = getColor();
        list.points.add(p);

        return list;
    }
}
