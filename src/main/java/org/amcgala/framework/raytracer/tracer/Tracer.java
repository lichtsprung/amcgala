package org.amcgala.framework.raytracer.tracer;

import org.amcgala.Scene;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.Ray;

/**
 * Methoden, die alle Raytracer zur Verfügung stellen müssen.
 */
public interface Tracer {
    RGBColor trace(Ray ray, Scene scene);
}
