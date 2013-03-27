package org.amcgala.framework.shape;

import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.renderer.DisplayList;
import org.amcgala.framework.shape.primitives.LinePrimitive;
import org.amcgala.framework.shape.primitives.TrianglePrimitive;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 3/22/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Triangle extends AbstractShape {
    protected Vertex3f a, b, c;
    private boolean filled = false;

    /**
     * Ein Dreieck, das über drei Punkte im Raum definiert wird.
     *
     * @param a Eckpunkt A
     * @param b Eckpunkt B
     * @param c Eckpunkt C
     */
    public Triangle(Vertex3f a, Vertex3f b, Vertex3f c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }


    public void filled(boolean filled) {
        this.filled = filled;
    }


    @Override
    public DisplayList getDisplayList() {
        DisplayList displayList = new DisplayList();
        if (filled) {
            TrianglePrimitive trianglePrimitive = new TrianglePrimitive(a, b, c);
            trianglePrimitive.color = getColor();
            displayList.triangles.add(trianglePrimitive);
        } else {
            LinePrimitive l1 = new LinePrimitive(a, b);
            LinePrimitive l2 = new LinePrimitive(b, c);
            LinePrimitive l3 = new LinePrimitive(c, a);
            l1.color = getColor();
            l2.color = getColor();
            l3.color = getColor();
            displayList.lines.add(l1);
            displayList.lines.add(l2);
            displayList.lines.add(l3);
        }
        return displayList;
    }
}
