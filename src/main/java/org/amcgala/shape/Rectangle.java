package org.amcgala.shape;

import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.shape.primitives.RectanglePrimitive;

/**
 * Ein Rechteckt
 */
public class Rectangle extends AbstractShape {
    private RectanglePrimitive p;


    public Rectangle(Vertex3f v, float width, float height) {
        p = new RectanglePrimitive(v, width, height);
    }

    @Override
    public DisplayList getDisplayList(DisplayList list) {
        p.color = getColor();
        list.rects.add(p);

        return list;
    }
}
