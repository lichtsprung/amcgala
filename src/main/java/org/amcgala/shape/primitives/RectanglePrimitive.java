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
}
