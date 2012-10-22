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
package org.amcgala.framework.shape;

import com.google.common.base.Objects;
import org.amcgala.Framework;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Pixel;
import org.amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eine Linie im 3d Raum.
 *
 * @author Robert Giacinto
 */
public class Line extends AbstractShape {
    private static final Logger log = LoggerFactory.getLogger(Line.class.getName());
    private double x1, y1, z1, x2, y2, z2;
    private Vector3d start, end;

    /**
     * Erstellt eine Linie, die in einer 2d Ebene mit z = -1 liegt und von (x1, y1) nach (x2, y2) geht.
     *
     * @param x1 x-Koordinate des Startvektors
     * @param y1 y-Koordinate des Startvektors
     * @param x2 x-Koordinate des Endvektors
     * @param y2 y-Koordinate des Endvektors
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        if (x1 > x2) { // vertausche Punkte
            this.x1 = x2;
            this.y1 = y2;
            this.x2 = x1;
            this.y2 = y1;
        }

        if (x1 == x2 && y1 > y2) { // Vertikale von y1 unten nach y2 oben
            this.y1 = y2;
            this.y2 = y1;
        }

        start = Vector3d.createVector3d(x1, y1, -1);
        end = Vector3d.createVector3d(x2, y2, -1);
    }

    /**
     * Erstellt eine Linie, die in einer 2d Ebene mit z = -1 liegt und von (x1, y1) nach (x2, y2) geht und explizit ein
     * Label zugewiesen bekommt.
     *
     * @param x1    x-Koordinate des Startvektors
     * @param y1    y-Koordinate des Startvektors
     * @param x2    x-Koordinate des Endvektors
     * @param y2    y-Koordinate des Endvektors
     * @param label das Label der Linie
     */
    public Line(double x1, double y1, double x2, double y2, String label) {
        this(x1, y1, x2, y2);
        this.label = label;
    }

    /**
     * Erstellt eine Linie zwischen zwei Vektoren.
     *
     * @param start der Startvektor
     * @param end   der Endvektor
     */
    public Line(Vector3d start, Vector3d end) {
        this.start = start;
        this.end = end;
        this.x1 = start.x;
        this.y1 = start.y;
        this.z1 = start.z;
        this.x2 = end.x;
        this.y2 = end.y;
        this.z2 = end.z;
    }

    /**
     * Erstellt eine Linie zwischen zwei Vektoren und weist dieser ein explizites Label zu.
     *
     * @param start der Startvektor
     * @param end   der Endvektor
     * @param label das Label der Linie
     */
    public Line(Vector3d start, Vector3d end, String label) {
        this(start, end);
        this.label = label;
    }

    @Override
    public void render(Renderer renderer) {
        if (Framework.getInstance().getActiveScene().hasLights()) {
            // Start- und Endpunkt der Linie in Pixeln, mit denen die Linienalgorithmen durchgefuehrt werden.
            Pixel startPixel = renderer.getPixel(start);
            Pixel endPixel = renderer.getPixel(end);

            // Wir zeichnen von links nach rechts. Sollte der Startpixel rechts vom Endpixel liegen, dann tauschen wir die Pixel.
            if (startPixel.x > endPixel.x) {
                Pixel tmp = startPixel;
                startPixel = endPixel;
                endPixel = tmp;
            }


            /*
            * Beginn des Bresenham Algorithmus
            */
            double dx = endPixel.x - startPixel.x;
            double dy = endPixel.y - startPixel.y;
            double dx2 = 2 * dx;
            double dy2 = 2 * dy;

            double e;
            double y = startPixel.y;
            double x = startPixel.x;
            double z = start.z;

            double dz = end.z - start.z;
            dz = Math.round(dz);

            int i = 1;
            Vector3d v = new Vector3d(x, y, z);
            renderer.drawPixel(v, appearance);

            //1.+8. Oktant
            if (dy <= dx && -dy <= dx) {
                e = Math.abs(dy2) - dx;
                while (i <= dx) {
                    if (e >= 0) { /*
                     * Diagonalschritt
                     */
                        if (dy <= 0) {
                            y--;
                        } else {
                            y++;
                        }
                        e -= dx2;
                    }
                    x++;
                    i++;
                    e += Math.abs(dy2);

                    z += (dz / dx);
                    v = new Vector3d(x, y, z);

                    renderer.drawPixel(v, appearance);
                }
            }

            //2.+7. Oktant
            if (Math.abs(dy) > dx) {
                e = dx2 - Math.abs(dy);
                while (i <= Math.abs(dy)) {
                    if (e >= 0) {
                        x++;
                        e -= Math.abs(dy2);
                    }
                    if (dy > 0) {
                        y++;
                    } else {
                        y--;
                    }
                    i++;
                    e += dx2;
                    z += (dz / dx);
                    v = new Vector3d(x, y, z);
                    renderer.drawPixel(v, appearance);
                }
            }
        } else {
            renderer.setColor(getColor());
            renderer.drawLine(start, end);
        }
    }

    /**
     * toString method.
     */
    public String toString() {
        return Objects.toStringHelper(getClass()).add("x1", x1).add("y1", y1).add("x2", x2).add("y2", y2).toString();
    }

    public Vector3d getStart() {
        return start;
    }

    public Vector3d getEnd() {
        return end;
    }
}
