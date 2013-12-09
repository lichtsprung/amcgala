package org.amcgala;

import com.google.common.base.Objects;

import java.awt.*;
import java.util.Random;

/**
 * RGB Color.
 *
 * @since 2.1
 */
public class RGBColor {
    public static final RGBColor BLACK = new RGBColor(0, 0, 0);
    public static final RGBColor WHITE = new RGBColor(1, 1, 1);
    public static final RGBColor RED = new RGBColor(1, 0, 0);
    public static final RGBColor GREEN = new RGBColor(0, 1, 0);
    public static final RGBColor BLUE = new RGBColor(0, 0, 1);
    private static final Random random = new Random(System.nanoTime());
    protected float red;
    protected float green;
    protected float blue;

    /**
     * Erzeugt ein neue neue RGB Farbe.
     *
     * @param red   der Rotanteil
     * @param green der Grünanteil
     * @param blue  der Blauanteil
     */
    public RGBColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Erzeugt einen neuen Grauwert.
     *
     * @param value der Grauwert
     */
    public RGBColor(float value) {
        this(value, value, value);
    }

    /**
     * Copy Konstruktor.
     *
     * @param color die Farbe, die kopiert werden soll
     */
    public RGBColor(RGBColor color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
    }

    /**
     * Addiert eine Farbe zu dieser hinzu und gibt eine neue Instanz mit der Summe zurück.
     *
     * @param other die zu addierende Farbe
     * @return die Summenfarbe
     */
    public RGBColor add(RGBColor other) {
        return new RGBColor(red + other.red, green + other.green, blue + other.blue);
    }

    /**
     * Multipliziert die Farbe mit einem Skalar.
     *
     * @param s der skalare Faktor
     * @return die resultierende Farbe
     */
    public RGBColor times(float s) {
        return new RGBColor(s * red, s * green, s * blue);
    }

    /**
     * Multipliziert komponentenweise zwei Farben.
     *
     * @param color die Farbe mit der multipliziert werden soll
     * @return die resultierende Farbe
     */
    public RGBColor times(RGBColor color) {
        return new RGBColor(red * color.red, green * color.green, blue * color.blue);
    }

    /**
     * Potenziert die Farbe mit einem Faktor.
     *
     * @param p der Exponent
     * @return die resultierende Farbe
     */
    public RGBColor pow(float p) {
        return new RGBColor((float) Math.pow(red, p), (float) Math.pow(green, p), (float) Math.pow(blue, p));
    }

    /**
     * Konvertiert die Instanz in ein Objekt der Klasse {@link Color}.
     *
     * @return die {@link Color} Instanz
     */
    public Color toAWTColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    /**
     * Gibt den Rotanteil der Farbe zurück.
     *
     * @return der Rotanteil
     */
    public float getRed() {
        if (red > 1.0f) return 1.0f;
        if (red < 0) return 0;
        return red;
    }

    /**
     * Gibt den Grünanteil der Farbe zurück.
     *
     * @return der Grünanteil
     */
    public float getGreen() {
        if (green > 1.0f) return 1.0f;
        if (green < 0) return 0;
        return green;
    }

    /**
     * Gibt den Blauanteil der Farbe zurück.
     *
     * @return der Blauanteil
     */
    public float getBlue() {
        if (blue > 1.0f) return 1.0f;
        if (blue < 0) return 0;
        return blue;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("red", red).add("green", green).add("blue", blue).toString();
    }

    public static RGBColor randomColour() {

        return new RGBColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }
}
