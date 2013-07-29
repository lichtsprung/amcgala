/*
 * Copyright 2011-2012 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.amcgala.renderer;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;

import java.awt.*;
import java.math.RoundingMode;

/**
 * Ein Pixel stellt einen Punkt in der Ausgabe dar.
 * Es wird während des Renderings verwendet, um ein AbstractShape über einen DefaultRenderer
 * auszugeben.
 *
 * @author Robert Giacinto
 */
public class Pixel {

    protected int x;
    protected int y;
    protected Color color = Color.BLACK;

    /**
     * Erzeugt einen neuen Pixel an der Stelle (x,y)
     *
     * @param x die x-Koordinate des Pixels
     * @param y die y-Koordinate des Pixels
     */
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Erzeugt einen neuen Pixel an der Stelle (x,y).
     * Die doubles werden entsprechend auf die Integerpositionen des Pixels gerundet.
     *
     * @param x die x-Koordinate des Pixels
     * @param y die y-Koordinate des Pixels
     */
    public Pixel(double x, double y) {
        this.x = DoubleMath.roundToInt(x, RoundingMode.HALF_DOWN);
        this.y = DoubleMath.roundToInt(y, RoundingMode.HALF_DOWN);
    }

    /**
     * Erzeugt einen neuen Pixel an der Stelle (x,y)
     *
     * @param x     die x-Koordinate des Pixels
     * @param y     die y-Koordinate des Pixels
     * @param color die Farbe des Pixels
     */
    public Pixel(int x, int y, Color color) {
        this(x, y);
        this.color = color;
    }

    /**
     * Erzeugt einen neuen Pixel an der Stelle (x,y).
     * Die doubles werden entsprechend auf die Integerpositionen des Pixels gerundet.
     *
     * @param x     die x-Koordinate des Pixels
     * @param y     die y-Koordinate des Pixels
     * @param color die Farbe des Pixels
     */
    public Pixel(double x, double y, Color color) {
        this(x, y);
        this.color = color;
    }

    /**
     * Gibt die x-Koordinate des Pixels zurück.
     *
     * @return die x-Koordinate des Pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Gibt die y-Koordinate des Pixels zurück.
     *
     * @return die y-Koordinate des Pixels
     */
    public int getY() {
        return y;
    }

    /**
     * Gibt die Farbe des Pixels zurück.
     *
     * @return die Farbe des Pixels
     */
    public Color getColor() {
        return color;
    }

    /**
     * Ändert die Farbe des Pixels
     *
     * @param color die neue Farbe des Pixels
     */
    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("x", x).add("y", y).add("Color", color).toString();
    }
}
