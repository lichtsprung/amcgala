package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

/**
 * Ein Punkt.
 */
public class PointPrimitive extends Primitive {
    public Vertex3f point;

    public PointPrimitive(Vertex3f point) {
        this.point = point;
    }
}
