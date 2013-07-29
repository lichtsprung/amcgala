package org.amcgala.raytracer.tracer;

import org.amcgala.Scene;
import org.amcgala.raytracer.RGBColor;
import org.amcgala.raytracer.Ray;

/**
 * Methoden, die alle Raytracer zur Verfügung stellen müssen.
 */
public interface Tracer {
    RGBColor trace(Ray ray, Scene scene);

    RGBColor trace(Ray ray, Scene scene, int depth);
}
