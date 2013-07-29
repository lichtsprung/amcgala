package org.amcgala.shape.primitives;

import org.amcgala.math.Vertex3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ein Primitiv.
 */
public abstract class Primitive {
    public List<Vertex3f> vertices = new ArrayList<Vertex3f>(2);
    public Color color = Color.black;
}
