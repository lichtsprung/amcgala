package org.amcgala.framework.shape;

import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.renderer.DisplayList;
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


    @Override
    public DisplayList getDisplayList() {
        DisplayList displayList = new DisplayList();
        TrianglePrimitive trianglePrimitive = new TrianglePrimitive(a, b, c);
        trianglePrimitive.color = getColor();
        displayList.triangles.add(trianglePrimitive);
        return displayList;
    }
}
