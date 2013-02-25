package org.amcgala.framework.shape.primitives;

import org.amcgala.framework.math.Vertex3f;

/**
 * Ein Quad.
 */
public class QuadPrimitive extends Primitive {
    public QuadPrimitive(Vertex3f v1, Vertex3f v2, Vertex3f v3, Vertex3f v4) {
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
    }
}
