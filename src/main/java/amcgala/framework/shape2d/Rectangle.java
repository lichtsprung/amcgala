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
package amcgala.framework.shape2d;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amcgala.framework.camera.Camera;
import amcgala.framework.lighting.Light;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;
import amcgala.framework.shape.Shape;

/**
 * Ein 2d-Rechteck.
 *
 * @author Robert Giacinto
 */
public class Rectangle extends Shape {

    public double width;
    public double height;
    public BresenhamLine bottom;
    public BresenhamLine top;
    public BresenhamLine left;
    public BresenhamLine right;

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
        bottom = new BresenhamLine(x, y, x + width, y);
        top = new BresenhamLine(x, y + height, x + width, y + height);
        left = new BresenhamLine(x, y, x, y + height);
        right = new BresenhamLine(x + width, y, x + width, y + height);
    }

    /**
     * Erzeugt ein neues Rechteck, das über die Kanten definiert wird.
     *
     * @param bottom die untere Seite des Rechtecks
     * @param left   die linke Seite des Rechtecks
     * @param top    die obere Seite des Rechtecks
     * @param right  die rechte Seite des Rechtecks
     * @deprecated
     */
    public Rectangle(BresenhamLine bottom, BresenhamLine left, BresenhamLine top, BresenhamLine right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        bottom.color = color;
        top.color = color;
        left.color = color;
        right.color = color;

        bottom.render(transformation, camera, renderer, lights);
        top.render(transformation, camera, renderer, lights);
        left.render(transformation, camera, renderer, lights);
        right.render(transformation, camera, renderer, lights);
    }

    @Override
    public String toString() {
        return "Rectangle2d{" + "bottom =" + bottom + ", top =" + top + ", left =" + left + ", right =" + right + '}';
    }
    
    private static final Logger log = LoggerFactory.getLogger(Rectangle.class.getName());
}
