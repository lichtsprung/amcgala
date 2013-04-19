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
package org.amcgala.framework.appearance;

import java.awt.*;

/**
 * Die Basisoberflächenklasse für alle Shapeobjekte.
 * Definiert die Oberflächeneigenschaften für ein ganzes Shapeobjekt,
 * würde normalerweise nur für ein Pixel gelten.
 *
 * @author Sascha Lemke
 */
public class DefaultAppearance implements Appearance {

    private double reflectionCoefficient = 1;
    private double specularCoefficient = 1;
    private double specularExponent = 2;
    private Color color = Color.BLACK;

    @Override
    public double getReflectionCoefficient() {
        return this.reflectionCoefficient;
    }

    @Override
    public double getSpecularCoefficient() {
        return this.specularCoefficient;
    }

    @Override
    public double getSpecularExponent() {
        return this.specularExponent;
    }

    @Override
    public void setReflectionCoefficient(double reflectionCoefficient) {
        this.reflectionCoefficient = reflectionCoefficient;
    }

    @Override
    public void setSpecularCoefficient(double specularCoefficient) {
        this.specularCoefficient = specularCoefficient;
    }

    @Override
    public void setSpecularExponent(double specularExponent) {
        this.specularExponent = specularExponent;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
