package org.amcgala.framework.raytracer.sampler;

import org.apache.commons.math3.random.MersenneTwister;

/**
 * Die Oberklasse aller Sampler, die vom Raytracer verwendet werden können.
 */
public abstract class AbstractSampler implements Sampler {
    protected int numberOfSamples = 1;
    protected MersenneTwister random = new MersenneTwister(System.nanoTime());

    @Override
    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    @Override
    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }
}
