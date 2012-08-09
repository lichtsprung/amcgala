/* 
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.amcgala.framework.lighting;

import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.math.Vector3d;

import java.awt.Color;


/**
 * Klasse zur Berechnung des ambienten Lichts.
 * Ambientes Licht fällt von allen Richtungen gleichstark auf ein Objekt.
 *
 * @author Sascha Lemke
 */
public class AmbientLight extends AbstractLight{



    /**
     * Konstruktor für das Ambientlight.
     *
     * @param label      Der Name der Lichtquelle
     * @param intensity Die Intensität der Lichtquelle
     */
    public AmbientLight(String label, double intensity, Color color) {
        this.label = label;
        if (intensity > 1 || intensity < 0) {
            throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
        } else {
            this.intensity = intensity;
        }
        this.color = color;
    }



    @Override
    public Color interpolate(Color color, Vector3d pixelposition, Vector3d camera, Appearance app) {
        /*
           * Berechnung des ambienten Lichts, die pixelposition wird hier nicht benötigt.
           *
           * Berechnet die Intensität der Farbkanäle.
           */
        double intensityRed = ((this.color.getRed() / 2.55) * this.intensity) / 100;
        double intensityGreen = ((this.color.getGreen() / 2.55) * this.intensity) / 100;
        double intensityBlue = ((this.color.getBlue() / 2.55) * this.intensity) / 100;

        /*
           * Berechnung des Reflexionskoeffzienten.
           */
        double reflectionRed = ((color.getRed() / 2.55) * app.getReflectionCoefficient()) / 100;
        double reflectionGreen = ((color.getGreen() / 2.55) * app.getReflectionCoefficient()) / 100;
        double reflectionBlue = ((color.getBlue() / 2.55) * app.getReflectionCoefficient()) / 100;

        /*
           * Berechnung der finalen Werte für die Farbkanäle.
           */
        float r = (float) (intensityRed * reflectionRed);
        float g = (float) (intensityGreen * reflectionGreen);
        float b = (float) (intensityBlue * reflectionBlue);

        return new Color(r, g, b);
    }

    /*
      * (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    public String toString() {
        return "AmbientLight: " + this.label + " { intensity: " + this.intensity + "; " + this.color.toString() + " }";
    }
}
