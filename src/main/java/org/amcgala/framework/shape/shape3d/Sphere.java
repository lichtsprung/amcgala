package org.amcgala.framework.shape.shape3d;

import org.amcgala.framework.math.MathConstants;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.HitResult;
import org.amcgala.framework.raytracer.Ray;
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
    public HitResult hit(Ray ray) {
        HitResult result = new HitResult();
        /*
         * Berechnen der Hilfsvariablen zur Bestimmung des Schnittpunkts zwischen Gerade und Kugel:
         *          t = (-b +- (b^2-4ac)^(1/2)/2 * a
         *          r(t) = origin + t * direction
         */
        Vector3d tmp = ray.origin.sub(center);
        double a = ray.direction.dot(ray.direction);
        double b = ray.direction.dot(tmp) * 2;
        double c = tmp.dot(tmp) - radius * radius;
        double discriminant = b * b - 4.0 * a * c;

        if (discriminant < 0) {
            return result;
        } else {
            double e = Math.sqrt(discriminant);
            double denominator = 2.0 * a;
            double t = (-b - e) / denominator;

            if (t > MathConstants.EPSILON) {
                result.t = t;
                result.normal = tmp.add(ray.direction.times(t)).times(1 / radius);
                result.hitPoint = ray.origin.add(ray.direction.times(t));
                return result;
            }

            t = (-b + e) / denominator;
            if (t > MathConstants.EPSILON) {
                result.t = t;
                result.normal = tmp.add(ray.direction.times(t)).times(1 / radius);
                result.hitPoint = ray.origin.add(ray.direction.times(t));
                return result;
            }
        }
        return result;
    }
}
