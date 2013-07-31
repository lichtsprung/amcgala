package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

/**
 * Ein Triangle.
 */
public class TrianglePrimitive extends Primitive {
    public Vertex3f v0, v1, v2;

    public TrianglePrimitive(Vertex3f v0, Vertex3f v1, Vertex3f v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }
}
