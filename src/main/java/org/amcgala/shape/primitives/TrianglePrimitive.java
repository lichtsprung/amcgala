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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrianglePrimitive that = (TrianglePrimitive) o;

        return v0.equals(that.v0) && v1.equals(that.v1) && v2.equals(that.v2);

    }

    @Override
    public int hashCode() {
        int result = v0.hashCode();
        result = 31 * result + v1.hashCode();
        result = 31 * result + v2.hashCode();
        return result;
    }
}
