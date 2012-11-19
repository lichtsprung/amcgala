package org.amcgala.framework.raytracer.material;

import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.ShadingInfo;
import org.amcgala.framework.raytracer.material.texture.Texture;

/**
 * Ein Material definiert die Oberflächeneigenschaften eines Objekts in der Szene.
 */
public class Material {
    protected Texture texture;
    protected RGBColor color = new RGBColor(0, 0, 0);

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public RGBColor getColor() {
        return color;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public RGBColor getColor(ShadingInfo hit) {
        RGBColor c = color;

        if (texture != null) {
            c = texture.getColor(hit);
        }

        return c;
    }
}
