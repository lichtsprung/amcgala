package org.amcgala.framework.raytracer.sampler;

import javax.vecmath.Point2d;

/**
 * Das Sampler Interface muss von allen Samplern des Raytracers implementiert werden. Ein Sampler stellt eine
 * Möglichkeit dar, die Sampling-Technik, die innerhalb des Raytracings verwendet wird, nach belieben auszutauschen.
 * Hierfür werden auf einem Einheitspixel die Samplingpunkte vorberechnet und können dann innerhalb der ViewPlane auf
 * beliebigen Pixelgrößen eingesetzt werden.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public interface Sampler {
    int getNumberOfSamples();
    void setNumberOfSamples(int numberOfSamples);
    Point2d getSamplingPoint();
}
