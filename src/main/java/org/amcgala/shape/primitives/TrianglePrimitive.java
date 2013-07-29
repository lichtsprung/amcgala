package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

/**
 * Ein Triangle.
 */
public class TrianglePrimitive extends Primitive {
    public TrianglePrimitive(Vertex3f v1, Vertex3f v2, Vertex3f v3) {
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
    }
}
