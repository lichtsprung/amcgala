package org.amcgala.framework.shape.primitives;

import org.amcgala.framework.math.Vertex3f;

/**
 * Linie von (x0, y0) nach (x1, y1).
 */
public class LinePrimitive extends Primitive {
    public LinePrimitive(Vertex3f v1, Vertex3f v2) {
        vertices.add(v1);
        vertices.add(v2);
    }
}
