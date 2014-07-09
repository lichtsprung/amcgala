package org.amcgala.math;

import com.google.common.base.Objects;

/**
 *
 */
public class Vertex3f {
    public float x, y, z;

    public Vertex3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("x", x).add("y", y).add("z", z).toString();
    }

    public Vector3d toVector() {
        return new Vector3d(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex3f vertex3f = (Vertex3f) o;

        return Float.compare(vertex3f.x, x) == 0 && Float.compare(vertex3f.y, y) == 0 && Float.compare(vertex3f.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
