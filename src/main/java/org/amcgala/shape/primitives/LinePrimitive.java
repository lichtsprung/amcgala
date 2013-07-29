package org.amcgala.shape.primitives;

import com.google.common.base.Objects;
import org.amcgala.math.Vertex3f;

/**
 * Linie von (x0, y0) nach (x1, y1).
 */
public class LinePrimitive extends Primitive {
    public LinePrimitive(Vertex3f v1, Vertex3f v2) {
        vertices.add(v1);
        vertices.add(v2);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Start", vertices.get(0)).add("End", vertices.get(1)).toString();
    }
}
