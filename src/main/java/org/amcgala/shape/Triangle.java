package org.amcgala.shape;

import org.amcgala.Framework;
import org.amcgala.FrameworkMode;
import org.amcgala.camera.Camera;
import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        // TODO ScanLine Algorithmus implementieren.
        throw new NotImplementedException();
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
