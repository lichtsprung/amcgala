package org.amcgala.framework.raytracer;

import org.amcgala.framework.math.Vector3d;

/**
 * Ein Strahl, der vom {@link RaytraceVisitor} in die {@link org.amcgala.Scene} geschickt wird. Er ist über die Vektorform d
 * definiert als r(t) = origin + t * direction.
 */
public class Ray {
    private Vector3d origin;
    private Vector3d direction;

    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
