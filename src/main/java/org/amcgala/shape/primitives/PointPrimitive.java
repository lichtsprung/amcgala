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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointPrimitive that = (PointPrimitive) o;

        return point.equals(that.point);

    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }
}
