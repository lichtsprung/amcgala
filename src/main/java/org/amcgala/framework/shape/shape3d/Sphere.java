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
        Vector3d tmp = ray.origin.sub(center);
        double a = ray.direction.dot(ray.direction);
        double b = ray.direction.dot(tmp) * 2;
        double c = tmp.dot(tmp) - radius * radius;
        double discriminant = b * b - 4.0 * a * c;

        if (discriminant >= 0) {
            double e = Math.sqrt(discriminant);
            double denominator = 2.0 * a;
            double t = (-b - e) / denominator;

            if (t > MathConstants.EPSILON) {
                shadingInfo.t = t;
                shadingInfo.normal = tmp.add(ray.direction.times(t)).times(1 / radius);
                shadingInfo.hitPoint = ray.origin.add(ray.direction.times(t));
                shadingInfo.color = material.getColor(shadingInfo);
                return true;
            } else {
                t = (-b + e) / denominator;
                if (t > MathConstants.EPSILON) {
                    shadingInfo.t = t;
                    shadingInfo.normal = tmp.add(ray.direction.times(t)).times(1 / radius);
                    shadingInfo.hitPoint = ray.origin.add(ray.direction.times(t));
                    shadingInfo.color = material.getColor(shadingInfo);
                    return true;
                }
            }
        }
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
