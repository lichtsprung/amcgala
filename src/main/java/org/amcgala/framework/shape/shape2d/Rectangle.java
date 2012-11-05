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

import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ein 2d-Rechteck.
 *
 * @author Robert Giacinto
 */
public class Rectangle extends AbstractShape {

    public double width;
    public double height;
    public Line bottom;
    public Line top;
    public Line left;
    public Line right;

    /**
     * Erzeugt ein neues Rechteck, über die Position der linken unteren Ecke und
     * der Höhe und Breite des Rechtecks.
     *
     * @param x      die x-Koordinate der Ecke links unten.
     * @param y      die y-Koordinate der Ecke links unten.
     * @param width  die Breite des Rechtecks
     * @param height die Höhe des Rechtecks
     */
    public Rectangle(double x, double y, double width, double height) {
        bottom = new Line(x, y, x + width, y);
        top = new Line(x, y + height, x + width, y + height);
        left = new Line(x, y, x, y + height);
        right = new Line(x + width, y, x + width, y + height);
    }

    /**
     * Erzeugt ein neues Rechteck, über die Position der linken unteren Ecke und
     * der Höhe und Breite des Rechtecks.
     *
     * @param x      die x-Koordinate der Ecke links unten.
     * @param y      die y-Koordinate der Ecke links unten.
     * @param width  die Breite des Rechtecks
     * @param height die Höhe des Rechtecks
     */
    public Rectangle(double x, double y, double width, double height, String label) {
        bottom = new Line(x, y, x + width, y);
        top = new Line(x, y + height, x + width, y + height);
        left = new Line(x, y, x, y + height);
        right = new Line(x + width, y, x + width, y + height);
        this.label = label;
    }


    @Override
    public void render(Renderer renderer) {
        bottom.setColor(getColor());
        top.setColor(getColor());
        left.setColor(getColor());
        right.setColor(getColor());

        bottom.render(renderer);
        top.render(renderer);
        left.render(renderer);
        right.render(renderer);
    }

    @Override
    public String toString() {
        return "Rectangle2d{" + "bottom =" + bottom + ", top =" + top + ", left =" + left + ", right =" + right + '}';
    }

    private static final Logger log = LoggerFactory.getLogger(Rectangle.class.getName());
}
