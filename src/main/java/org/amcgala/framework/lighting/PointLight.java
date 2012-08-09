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
 * Klasse für das Punktlicht, dient zur Berechnung einer Lichtquelle die in
 * alle Richtungen von einem bestimmten Punkt aus Licht abstrahlt und das
 * mit der Entfernung schwächer wird.
 *
 * @author Sascha Lemke
 */
public class PointLight extends AbstractLight {

    private AmbientLight ambient;


    /**
     * QuickKonstruktor, erstellt ein Licht mit den Basiseinstellungen und möglichst wenig Parametern.
     *
     * @param label    Der Name der Lichtquelle
     * @param ambient  Das ambiente Licht
     * @param position Die Position der Lichtquelle
     */
    public PointLight(String label, AmbientLight ambient, Vector3d position) {
        this.label = label;
        this.ambient = ambient; // muss nicht geprüft werde, da dies schon beim ambienten Licht passiert.
        this.position = position;
    }

    /**
     * Konstruktor.
     *
     * @param label        Der Name der Lichtquelle
     * @param ambientColor Die Farbe des ambienten Lichts
     * @param position     Die Position des Pointlights
     */
    public PointLight(String label, double ambientIntensity, Color ambientColor, Vector3d position, Color color) {
        this.label = label;
        if (ambientIntensity > 1 || ambientIntensity < 0) {
            throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
        } else {
            this.ambient.setIntensity(ambientIntensity);
        }
        this.ambient.setColor(ambientColor);
        this.position = position;
        this.color = color;
    }




    @Override
    public Color interpolate(Color color, Vector3d oberflaechennormale, Vector3d camera, Appearance appearance) {
        Vector3d normiert = oberflaechennormale.copy();
        normiert.normalize();

        double angle = this.position.dot(oberflaechennormale);

        /*
           * Berechnung der ambienten Intensität.
           */
        double ambientIntensityRed = ((this.ambient.getColor().getRed() / 2.55) * this.ambient.getIntensity()) / 100;
        double ambientIntensityGreen = ((this.ambient.getColor().getGreen() / 2.55) * this.ambient.getIntensity()) / 100;
        double ambientIntensityBlue = ((this.ambient.getColor().getBlue() / 2.55) * this.ambient.getIntensity()) / 100;

        /*
           * Berechnung der Reflexion.
           */
        double reflectionRed = ((color.getRed() / 2.55) * appearance.getReflectionCoefficient()) / 100;
        double reflectionGreen = ((color.getGreen() / 2.55) * appearance.getReflectionCoefficient()) / 100;
        double reflectionBlue = ((color.getBlue() / 2.55) * appearance.getReflectionCoefficient()) / 100;

        if (angle > 0) {

            /*
                * Berechnung der Punktlichtintensität.
                */
            double pointIntensityRed = ((this.color.getRed() / 2.55) * this.intensity) / 100;
            double pointIntensityGreen = ((this.color.getGreen() / 2.55) * this.intensity) / 100;
            double pointIntensityBlue = ((this.color.getBlue() / 2.55) * this.intensity) / 100;

            /*
                * Berechnung des Austrittsvektors
                */
            Vector3d rj = normiert.times(normiert.dot(this.position));

            /*
                * Berechnung der Spiegelreflexion
                */
            double result = Math.pow(rj.dot(camera), appearance.getSpecularExponent());

            double specularRed = pointIntensityRed * appearance.getSpecularCoefficient() * result;
            double specularGreen = pointIntensityGreen * appearance.getSpecularCoefficient() * result;
            double specularBlue = pointIntensityBlue * appearance.getSpecularCoefficient() * result;

            /*
                * Berechnung der Distanz von dem Pixel zur Lichtquelle.
                */
            Vector3d distanceVector = this.position.sub(normiert);
            double distance = Math.sqrt(Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.z, 2));

            /*
                * Berechnung der Abschwächung.
                */
            double attenuation = Math.min(1, 1 / (this.constantAttenuation + this.linearAttenuation * distance + this.exponentialAttenuation * Math.pow(distance, 2)));

            /*
                * Berechnung der finalen Farbwerte.
                */
            float r = (float) ((ambientIntensityRed * reflectionRed) + ((pointIntensityRed * reflectionRed) * angle + specularRed) * attenuation);
            float g = (float) ((ambientIntensityGreen * reflectionGreen) + ((pointIntensityGreen * reflectionGreen) * angle + specularGreen) * attenuation);
            float b = (float) ((ambientIntensityBlue * reflectionBlue) + ((pointIntensityBlue * reflectionBlue) * angle + specularBlue) * attenuation);

            /*
                * Abfangen möglicher Rundungsfehler.
                */
            if (r > 1) r = 1;
            if (g > 1) g = 1;
            if (b > 1) b = 1;

            return new Color(r, g, b);
        } else {

            /*
                * ambientes Licht für die Seite die dem Licht nicht zugewandt ist.
                */
            float r = (float) (ambientIntensityRed * reflectionRed);
            float g = (float) (ambientIntensityGreen * reflectionGreen);
            float b = (float) (ambientIntensityBlue * reflectionBlue);

            return new Color(r, g, b);
        }
    }


    @Override
    public String toString() {
        String output = "";
        output += "Punktlicht: " + this.label;
        output += " { \n";
        output += "\t ambiente Intensität: " + this.ambient.getIntensity() + "; \n";
        output += "\t ambiente Farbe: " + this.ambient.getColor().toString() + "; \n";
        output += "\t Position: " + this.position.toString() + " \n";
        output += "\t Farbe des Punktlichts: " + this.color.toString() + "; \n";
        output += "\t Intensität des Punktlichts: " + this.intensity + "; \n";
        output += "\t Konstante Abschwächung: " + this.constantAttenuation + "; \n";
        output += "\t Lineare Abschwächung: " + this.linearAttenuation + "; \n";
        output += "\t Exponentielle Abschwächung: " + this.exponentialAttenuation + "; \n}";
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointLight that = (PointLight) o;

        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
