package org.amcgala.framework.shape.util.bounds;

import org.amcgala.framework.math.Vector3d;

import java.util.List;

/**
 * Eine BoundingBox umschließt das Volumen eines {@link org.amcgala.framework.shape.Shape} Objekts.
 * Die Bounding Box umschließt das Volumen der Punkte und kann für Kollisionsbestimmungen etc. eingesetzt werden.
 *
 * @author Robert Giacinto
 */
public class BoundingBox {
    private Vector3d center;
    private double width;
    private double height;
    private double depth;

    /**
     * Erzeugt eine Bounding Box um ein Objekt, das durch eine Anzahl von Punkten definiert ist.
     * Die Bounding Box umschließt das Volumen der Punkte und kann für Kollisionsbestimmungen etc. eingesetzt werden.
     *
     * @param vectors die Punkte des Objekts
     */
    public BoundingBox(List<Vector3d> vectors) {
       updateBox(vectors);
    }

    public BoundingBox(){
        center = Vector3d.ZERO;
    }

    public void updateBox(List<Vector3d> vectors) {
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double zMin = Double.MAX_VALUE;

        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        double zMax = Double.MIN_VALUE;

        for (Vector3d v : vectors) {
            if (v.x < xMin) {
                xMin = v.x;
            }
            if (v.y < yMin) {
                yMin = v.y;
            }
            if (v.z < zMin) {
                zMin = v.z;
            }
            if (v.x > xMax) {
                xMax = v.x;
            }
            if (v.y > yMax) {
                yMax = v.y;
            }
            if (v.z > zMax) {
                zMax = v.z;
            }
        }

        center = new Vector3d(xMin + xMax, yMin + yMax, zMin + zMax);
        center = center.times(0.5);

        width = xMax - center.x;
        height = yMax - center.y;
        depth = zMax - center.z;
    }

    public Vector3d getCenter() {
        return center;
    }

    public double getWidth() {
        return width * 2;
    }

    public double getHeight() {
        return height * 2;
    }

    public double getDepth() {
        return depth * 2;
    }
}
