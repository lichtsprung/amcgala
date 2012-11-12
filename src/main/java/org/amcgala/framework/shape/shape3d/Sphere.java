package org.amcgala.framework.shape.shape3d;

import org.amcgala.framework.math.MathConstants;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.Ray;
import org.amcgala.framework.raytracer.ShadingInfo;
import org.amcgala.framework.shape.AbstractShape;

/**
 * Eine Kugel, über einen Raytracer dargestellt werden kann.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class Sphere extends AbstractShape {
    private Vector3d center;
    private double radius;


    public Sphere(Vector3d center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean hit(Ray ray, ShadingInfo shadingInfo) {
        shadingInfo.ray = ray;
        shadingInfo.label = getLabel();

        /*
         * TODO
         * Schnittpunktberechnung Gerade - Kugel.
         *
         * Verwenden Sie die Zusatzinformationen von der Website, um die Berechnung korrekt zu implementieren.
         * Im Objekt shadingInfo sollten folgende Informationen gespeichert werden:
         *      - t
         *      - die Normale
         *      - der Schnittpunkt
         *      - die Farbe der Kugel
         */


        return false;
    }

    public Vector3d getCenter() {
        return center;
    }

    public void setCenter(Vector3d center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
