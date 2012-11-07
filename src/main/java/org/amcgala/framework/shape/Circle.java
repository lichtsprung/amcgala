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

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ein 3d Kreis.
 *
 * @author Steffen Tröster
 * @author Robert Giacinto
 */
public class Circle extends AbstractShape {

    private double x, y;
    private double radius;
    private Vector3d pos;
    private double z;

    /**
     * Ein Kreis im 3d-Raum mit dem Mittelpunkt an der Position (x,y,z).
     *
     * @param x      die x-Position des Mittelpunkts
     * @param y      die y-Position des Mittelpunkts
     * @param z      die z-Position des Mittelpunkts
     * @param radius der Radius
     */
    public Circle(double x, double y, double z, double radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        pos = Vector3d.createVector3d(x, y, z);
    }

    /**
     * Ein Kreis im 3d-Raum mit einem Vektor als Mittelpunkt.
     *
     * @param middle Mittelpunkt als Vektor
     * @param radius der Radius
     */
    public Circle(Vector3d center, double radius) {
        this.x = center.x;
        this.y = center.y;
        this.z = center.z;
        this.radius = radius;
        pos = Vector3d.createVector3d(x, y, z);
    }

    /**
     * Setzt den Radius des Kreises auf den übergebenen Wert.
     *
     * @param r der neue Radius des Kreises
     */
    public void setRadius(double r) {
        this.radius = r;
    }

    /**
     * Gibt den Radius des Kreises zurück.
     *
     * @return der Radius des Kreises
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Setzt den Kreis auf die übergebene Position.
     */
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Setzt den Kreis auf die übergebene Position.
     */
    public void setPosition(Vector3d center) {
        this.x = center.x;
        this.y = center.y;
        this.z = center.z;
    }

    /**
     * Gibt die Position des Kreises zurück.
     *
     * @return die Position des Kreises
     */
    public Vector3d getPosition() {
        return Vector3d.createVector3d(this.x, this.y, this.z);
    }


    @Override
    public void render(Renderer renderer) {
        pos = Vector3d.createVector3d(x, y, z);
        renderer.setColor(getColor());
        renderer.drawCircle(pos, radius);
    }


    @Override
    public String toString() {
        return "Circle3d{" + "x=" + x + ", y=" + y + ", radius=" + radius
                + ", pos=" + pos + '}';
    }

    private static final Logger log = LoggerFactory.getLogger(Circle.class.getName());
}
