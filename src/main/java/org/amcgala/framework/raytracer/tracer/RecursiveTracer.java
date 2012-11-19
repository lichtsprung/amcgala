package org.amcgala.framework.raytracer.tracer;

import org.amcgala.Scene;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.Ray;

/**
 * Rekursiver Raytracer, der für die Berechnung von Reflexionen verwendet werden kann.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RecursiveTracer implements Tracer {
    private int maxDepth;

    public RecursiveTracer(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public RGBColor trace(Ray ray, Scene scene) {
        return trace(ray, scene, 0);
    }

    @Override
    public RGBColor trace(Ray ray, Scene scene, int depth) {
        /*
         * TODO Die rekursive Version des Tracers aus Aufgabe 7.
         */

        return null;
    }
}
