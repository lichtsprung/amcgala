package org.amcgala.framework.lighting;

import org.amcgala.framework.math.Vector3d;

import java.awt.Color;

/**
 * Oberklasse aller Lichter im Framework.
 *
 * @author Robert Giacinto
 */
public abstract class AbstractLight implements Light {
    protected String label = getClass().getSimpleName() + " - " + System.nanoTime();

    // ambiente elemente
    protected AmbientLight ambient;

    // pointelemente
    protected Vector3d position;
    protected double intensity = 1;

    // Spotlight werte
    protected Vector3d direction;
    protected Color color = new Color(255, 255, 255);

    // Attenuation
    protected double constantAttenuation = 0;
    protected double linearAttenuation = 0;
    protected double exponentialAttenuation = 1;


    /**
     * Gibt die Position des Pointlights zurück.
     *
     * @return Die Position des Pointlights
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * @param position
     */
    public void setPosition(Vector3d position) {
        this.position = position;
    }

    /**
     * Gibt die Intensiät des Pointlights zurück.
     *
     * @return Die Intensität des Pointlights
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Setzt die Intensität des Pointlights auf den übergebenen Wert.
     *
     * @param pointIntensity Die Intensität des Pointlights
     */
    public void setIntensity(double pointIntensity) {
        if (pointIntensity > 1 || pointIntensity < 0) {
            throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
        } else {
            this.intensity = pointIntensity;
        }
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gibt die Farbe des Pointlights zurück.
     *
     * @return Die Farbe
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Setzt die Farbe des Pointlights.
     *
     * @param color Die Farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gibt die konstante Lichtabschwächung zurück.
     *
     * @return the constantAttenuation die konstante Lichtabschwächung
     */
    public double getConstantAttenuation() {
        return constantAttenuation;
    }

    /**
     * Setzt die konstante Lichtabschächung auf den übergebenen Wert.
     *
     * @param constantAttenuation the constantAttenuation to set
     */
    public void setConstantAttenuation(double constantAttenuation) {
        this.constantAttenuation = constantAttenuation;
    }

    /**
     * Gibt die lineare Lichtabschwächung zurück.
     *
     * @return the linearAttenuation die lineare Lichtabschwächung
     */
    public double getLinearAttenuation() {
        return linearAttenuation;
    }

    /**
     * Setzt die lineare Lichtabschwächung auf den übergebenen Wert.
     *
     * @param linearAttenuation the linearAttenuation to set
     */
    public void setLinearAttenuation(double linearAttenuation) {
        this.linearAttenuation = linearAttenuation;
    }

    /**
     * Gibt die exponentielle Lichtabschwächung zurück.
     *
     * @return the exponentialAttenuation die exponentielle Lichtabschwächung
     */
    public double getExponentialAttenuation() {
        return exponentialAttenuation;
    }

    /**
     * Setzt die exponentielle Lichtabschwächung auf den übergebenen Wert.
     *
     * @param exponentialAttenuation the exponentialAttenuation to set
     */
    public void setExponentialAttenuation(double exponentialAttenuation) {
        this.exponentialAttenuation = exponentialAttenuation;
    }
}
