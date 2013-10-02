package org.amcgala.shape.primitives;

import com.google.common.base.Objects;
import org.amcgala.math.Vertex3f;

/**
 * Linie von (x0, y0) nach (x1, y1).
 */
public class LinePrimitive extends Primitive {
    public Vertex3f v0;
    public Vertex3f v1;

    public LinePrimitive(Vertex3f v0, Vertex3f v1) {
        this.v0 = v0;
        this.v1 = v1;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Start", v0).add("End", v1).toString();
    }
}
