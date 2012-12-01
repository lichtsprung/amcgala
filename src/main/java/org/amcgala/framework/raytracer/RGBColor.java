package org.amcgala.framework.raytracer;

import com.google.common.base.Objects;

import java.awt.*;

/**
 * RGB Color Darstellung für Raytracing.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RGBColor {
    protected float red;
    protected float green;
    protected float blue;

    public RGBColor(float red, float green, float blue) {

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public RGBColor(RGBColor color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
    }

    public RGBColor add(RGBColor other) {
        return new RGBColor(red + other.red, green + other.green, blue + other.blue);
    }

    public RGBColor times(float s) {
        return new RGBColor(s * red, s * green, s * blue);
    }

    public RGBColor times(RGBColor color) {
        return new RGBColor(red * color.red, green * color.green, blue * color.blue);
    }

    public RGBColor pow(float p) {
        return new RGBColor((float) Math.pow(red, p), (float) Math.pow(green, p), (float) Math.pow(blue, p));
    }

    public Color toColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    public float getRed() {
        if(red > 1.0f) return 1.0f;
        if(red < 0) return 0;
        return red;
    }

    public float getGreen() {
        if(green > 1.0f) return 1.0f;
        if(green < 0) return 0;
        return green;
    }

    public float getBlue() {
        if(blue > 1.0f) return 1.0f;
        if(blue < 0) return 0;
        return blue;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("red", red).add("green", green).add("blue", blue).toString();
    }
}
