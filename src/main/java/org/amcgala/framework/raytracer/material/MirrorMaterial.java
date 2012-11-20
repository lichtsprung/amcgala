package org.amcgala.framework.raytracer.material;

import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.ShadingInfo;

/**
 * Spiegelndes Material für den Raytracer.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class MirrorMaterial extends Material {
    private float reflectionCoefficient;
    private RGBColor baseColor;

    /**
     * Ein reflektives Material, das die Umgebung spiegelt.
     *
     * @param reflectionCoefficient der Reflektionskoeffizient
     * @param baseColor             die Grundfarbe des Materials
     */
    public MirrorMaterial(float reflectionCoefficient, RGBColor baseColor) {
        this.reflectionCoefficient = reflectionCoefficient;
        this.baseColor = baseColor;
    }

    @Override
    public RGBColor getColor(ShadingInfo hit) {

        /*
         * TODO An dieser Stelle soll die Reflektion berechnet werden.
         *
         * Hierfür muss die eigene Farbe des Objekts an der Stelle des Hitpoints berechnet werden.
         * Dazu addiert werden die Reflektionsfarben der Objekte, die über die Reflektionsstrahlen getroffen werden.
         * Die gewichtete Summe der Farben wird am Ende der Methode zurückgegeben.
         *
         */

        return null;
    }
}
