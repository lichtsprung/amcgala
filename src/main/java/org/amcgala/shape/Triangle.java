package org.amcgala.shape;

import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.camera.Camera;
import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Dreieck.
 * C
 * /\
 * /  \
 * /    \
 * /      \
 * /        \
 * /          \
 * /            \
 * /              \
 * A----------------B
 */
public class Triangle extends AbstractShape {
    protected Vertex3f a, b, c;
    private Framework framework = Framework.getInstance();
    private Camera camera = framework.getActiveScene().getCamera();
    private boolean fill = false;

    /**
     * Ein Dreieck, das über drei Punkte im Raum definiert wird.
     *
     * @param a Eckpunkt A
     * @param b Eckpunkt B
     * @param c Eckpunkt C
     */
    public Triangle(Vertex3f a, Vertex3f b, Vertex3f c) {
        // TODO sortieren
        this.a = a;
        this.b = b;
        this.c = c;
    }


    /**
     * Ob das Polygon gefüllt oder im Wireframe-Modus gezeichnet werden soll.
     *
     * @param fill true, wenn Polygon gefüllt werden soll
     */
    public void fill(boolean fill) {
        this.fill = fill;
    }

    /**
     * Berechnet die Scan Lines, mit denen das Dreieck auf dem Bildschirm im Software Modus gefüllt werden kann.
     *
     * @return die Liste der Scan Lines
     */
    protected List<Line> scanline() {
        Vertex3f a = this.a, b = this.b, c = this.c;

        float dx1, dx2, dx3;
        if (b.y - a.y > 0) dx1 = (b.x - a.x) / (b.y - a.y);
        else dx1 = 0;
        if (c.y - a.y > 0) dx2 = (c.x - a.x) / (c.y - a.y);
        else dx2 = 0;
        if (c.y - b.y > 0) dx3 = (c.x - b.x) / (c.y - b.y);
        else dx3 = 0;

        ArrayList<Line> lines = new ArrayList<Line>();

        Vertex3f left = a, right = a;

        if (dx1 > dx2) {
            for (; left.y <= b.y; left.y++, right.y++, left.x += dx2, right.x += dx1) {
                lines.add(new Line(new Vertex3f(left.x, left.y, 0), new Vertex3f(right.x, left.y, 0)));
            }
            right = b;
            for (; left.y <= c.y; left.y++, right.y++, left.x += dx2, right.x += dx3) {
                lines.add(new Line(new Vertex3f(left.x, left.y, 0), new Vertex3f(right.x, left.y, 0)));
            }
        } else {
            for (; left.y <= b.y; left.y++, right.y++, left.x += dx1, right.x += dx2) {
                lines.add(new Line(new Vertex3f(left.x, left.y, 0), new Vertex3f(right.x, left.y, 0)));
            }
            left = b;
            for (; left.y <= c.y; left.y++, right.y++, left.x += dx3, right.x += dx2) {
                lines.add(new Line(new Vertex3f(left.x, left.y, 0), new Vertex3f(right.x, left.y, 0)));
            }
        }
        return lines;
    }


    @Override
    public DisplayList getDisplayList() {
        final DisplayList displayList = new DisplayList();
        if (fill) {
            if (Framework.currentMode == FrameworkMode.GL) {
                TrianglePrimitive trianglePrimitive = new TrianglePrimitive(a, b, c);
                trianglePrimitive.color = getColor();
                displayList.triangles.add(trianglePrimitive);
            } else if (Framework.currentMode == FrameworkMode.SOFTWARE) {
                List<Line> scanLines = scanline();
                for (Line l : scanLines) {
                    displayList.add(l.getDisplayList());
                }
            }

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