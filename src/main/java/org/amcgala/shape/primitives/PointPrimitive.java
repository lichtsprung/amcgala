package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

/**
 * Ein Punkt.
 */
public class PointPrimitive extends Primitive {
    public PointPrimitive(Vertex3f v1) {
        vertices.add(v1);
    }
}
