package org.amcgala.shape.util.bounds;

import com.google.common.base.Objects;
import org.amcgala.math.Matrix;
import org.amcgala.math.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Eine BoundingBox umschließt das Volumen eines {@link org.amcgala.shape.Shape} Objekts.
 * Die Bounding Box umschließt das Volumen der Punkte und kann für Kollisionsbestimmungen etc. eingesetzt werden.
 *
 * @author Robert Giacinto
 */
public class BoundingBox {
    private static final Logger log = LoggerFactory.getLogger(BoundingBox.class);
    private Vector3d center;
    private double width;
    private double height;
    private double depth;
    private List<Vector3d> vectors;
    private boolean debug;

    /**
     * Erzeugt eine Bounding Box um ein Objekt, das durch eine Anzahl von Punkten definiert ist.
     * Die Bounding Box umschließt das Volumen der Punkte und kann für Kollisionsbestimmungen etc. eingesetzt werden.
     *
     * @param vectors die Punkte des Objekts
     */
    public BoundingBox(List<Vector3d> vectors) {
        updateBox(vectors);
    }

    public BoundingBox() {
        center = Vector3d.ZERO;
    }

    public void updateBox(List<Vector3d> vectors) {
        this.vectors = vectors;
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

        width *= 2;
        height *= 2;
        depth *= 2;
    }

    public void updateBox(Matrix transform) {
        double scaleX = transform.get(0, 0);
        double scaleY = transform.get(1, 1);
        double scaleZ = transform.get(2, 2);

        center = center.transform(transform);
        width *= scaleX;
        height *= scaleY;
        depth *= scaleZ;
    }

    public Vector3d getCenter() {
        return center;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(BoundingBox.class)
                .add("Center", center)
                .add("Width", width)
                .add("Height", height)
                .add("Depth", depth).toString();
    }
}
