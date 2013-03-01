package org.amcgala.framework.math;

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
}
