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
package org.amcgala.framework.shape.shape2d;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A 2D Circle. Drawn with the Bresenham Algorithm.
 *
 * @author Steffen Troester
 */
public class Circle extends Shape {

    private double x;
    private double y;
    private double radius;
    private Vector3d pos;

    /**
     * Ein Kreis an der Position (x,y).
     *
     * @param x      x-Position des Mittelpunkts
     * @param y      y-Position des Mittelpunkts.
     * @param radius der Radius
     */
    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        pos = new Vector3d(x, y, -1);
    }

    /**
     * Setzt den Radius des Kreises auf den übergebenen Wert.
     *
     * @param r
     */
    public void setRadius(double r) {
        this.radius = r;
    }

    /**
     * Gibt den Radius des Kreises zurück.
     *
     * @return
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Setzt die Position des Kreises auf den übergebenen Wert.
     *
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gibt den Wert <i>x</i> des Kreises zurück.
     *
     * @return
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gibt den Wert <i>y</i> des Kreises zurück.
     *
     * @return
     */
    public double getY() {
        return this.y;
    }

    @Override
    public void render(Renderer renderer) {
        pos = new Vector3d(x, y, -1);
        renderer.drawCircle(pos, radius);
    }

    @Override
    public String toString() {
        return "Circle2d{" + "x=" + x + ", y=" + y + ", radius=" + radius
                + ", pos=" + pos + '}';
    }

    private static final Logger log = LoggerFactory.getLogger(Circle.class);
}