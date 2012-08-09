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

import java.awt.Color;

/**
 * Klasse das Informationen zur Oberfläche eines Objektes speichert, wie z. B. den Reflexionskoeffizienten.
 *
 * @author Sascha Lemke
 */
public interface Appearance {

    /**
     * Gibt den Reflexionskoeffizienten zurück.
     *
     * @return Der Reflexionskoeffizient
     */
    double getReflectionCoefficient();

    /**
     * Gibt den Spiegelkoeffizienten zurück.
     *
     * @return der Spiegelkoeffizient
     */
    double getSpecularCoefficient();

    /**
     * Gibt den Spiegelreflexionskoeffizient.
     *
     * @return der Spiegelreflexionskoeffizient.
     */
    double getSpecularExponent();

    /**
     * Gibt die Farbe zurück.
     *
     * @return die Farbe
     */
    Color getColor();
}
