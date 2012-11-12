package org.amcgala.framework.raytracer.sampler;

import javax.vecmath.Point2d;

/**
 * Sampled in der Mitte eines Pixels.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RegularSampler extends AbstractSampler {
    private static final Point2d p = new Point2d(0.5, 0.5);
    @Override
    public Point2d getSamplingPoint() {
        return p;
    }
}
