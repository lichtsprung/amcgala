package org.amcgala.framework.raytracer.tracer;

import org.amcgala.Scene;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.Ray;
import org.amcgala.framework.raytracer.ShadingInfo;
import org.amcgala.framework.shape.Shape;

/**
 * Rekursiver Raytracer, der für die Berechnung von Reflexionen verwendet werden kann.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RecursiveTracer implements Tracer {
    private int maxDepth;

    /**
     * Erzeugt einen neuen rekursiv arbeitenden Tracer. Die Rekursionstiefe bestimmt, wie oft von einem Schnittpunkt ein
     * neuer Strahl in die Szene geschickt wird.
     *
     * @param maxDepth die Anzahl von Bounds innerhalb der Szene
     */
    public RecursiveTracer(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public RGBColor trace(Ray ray, Scene scene) {
        return trace(ray, scene, 0);
    }

    @Override
    public RGBColor trace(Ray ray, Scene scene, int depth) {
        if (depth > maxDepth) {
            return new RGBColor(0, 0, 0);
        } else {
            ShadingInfo result = new ShadingInfo();
            result.color = scene.getBackground();

            for (Shape shape : scene.getShapes()) {
                ShadingInfo tmp = new ShadingInfo();
                tmp.tracer = this;
                tmp.scene = scene;
                tmp.depth = depth;
                tmp.label = shape.getLabel();
                shape.hit(ray, tmp);

                if (tmp.t < result.t) {
                    result = tmp;
                }
            }
            return result.color;
        }
    }
}
