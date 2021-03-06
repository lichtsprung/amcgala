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
package org.amcgala.animation.interpolation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface, das alle Klassen implementieren, die eine Interpolation
 * darstellen.
 *
 * @author Robert Giacinto
 */
public abstract class Interpolation {

    private static final Logger log = LoggerFactory.getLogger(Interpolation.class);
    protected double min;
    protected double max;
    protected double stepCount;
    protected double stepCounter;
    protected boolean cyclic;

    /**
     * Superkonstruktor aller Interpolationen. Er initialisiert die gemeinsamen Felder.
     *
     * @param start     Startwert der Interpolation
     * @param end       Endwert der Interpolation
     * @param stepCount die Anzahl der Schritte
     * @param cyclic    true, wenn Interpolation zyklisch von Neuem beginnen soll
     */
    public Interpolation(double start, double end, int stepCount, boolean cyclic) {
        this.min = start;
        this.max = end;
        this.stepCount = stepCount;
        this.cyclic = cyclic;
    }

    /**
     * Diese Methode wird von allen Unterklassen implementiert und gibt den nächsten interpolierten Wert zurück.
     *
     * @return der nächste interpolierte Wert
     */
    public abstract double nextValue();
}
