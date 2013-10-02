package org.amcgala.raytracer.sampler;

import javax.vecmath.Point2d;

/**
 * Sampler, der n zufällige Punkte im Pixel sampled.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RandomSampler extends AbstractSampler {

    public RandomSampler(int samplingCount) {
        setNumberOfSamples(samplingCount);
    }

    @Override
    public Point2d getSamplingPoint() {
        return new Point2d(random.nextDouble(), random.nextDouble());
    }
}
