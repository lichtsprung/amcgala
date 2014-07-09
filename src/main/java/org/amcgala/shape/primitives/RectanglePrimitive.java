package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

/**
 * Ein Rechteck.
 */
public class RectanglePrimitive extends Primitive {
    public Vertex3f v0;
    public float width;
    public float height;


    public RectanglePrimitive(Vertex3f v0, float width, float height) {
        this.v0 = v0;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectanglePrimitive that = (RectanglePrimitive) o;

        return Float.compare(that.height, height) == 0 && Float.compare(that.width, width) == 0 && v0.equals(that.v0);

    }

    @Override
    public int hashCode() {
        int result = v0.hashCode();
        result = 31 * result + (width != +0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }
}
