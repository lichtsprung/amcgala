package org.amcgala.framework.raytracer.material.texture;

import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.ShadingInfo;

/**
 * Jede Textur, die von dem Raytracer dargestellt werden soll, muss dieses Interface implementieren.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public interface Texture {
    RGBColor getColor(ShadingInfo hit);
}
