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
package amcgala.framework.shape;

import java.util.logging.Logger;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Renderer;

/**
 * Zeichnet eine Triangle im 2D Raum.
 * @author Sascha Lemke
 */
public class Triangle2d extends Shape {

    private BresenhamLine2d a;
    private BresenhamLine2d b;
    private BresenhamLine2d c;
    
    /**
     * Konstruktor, zeichnet die Triangle mit fertigen Linien.
     * @param a
     * @param b
     * @param c
     */
    public Triangle2d(BresenhamLine2d a, BresenhamLine2d b, BresenhamLine2d c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }    
    
    /**
     * Konstruktor, erlaubt es eine Triangle mit Werten statt fertigen Linien zu zeichnen.
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @param cx
     * @param cy
     */
    public Triangle2d(double ax, double ay, double bx, double by, double cx, double cy) {
        a = new BresenhamLine2d(cx, cy, bx, by);
        b = new BresenhamLine2d(ax, ay, cx, cy);
        c = new BresenhamLine2d(ax, ay, bx, by);
    }
    
    /**
     * Übernimmt die Linie für die Linie a.
     */
    public void setA(BresenhamLine2d a) {
    	this.a = a;
    }
    
    /**
     * Gibt die Linie a zurück.
     * @return
     */
    public BresenhamLine2d getA() {
    	return a;
    }
    
    /**
     *  Übernimmt die Linie für die Linie b.
     * @param b
     */
    public void setB(BresenhamLine2d b) {
    	this.b = b;
    }
    
    /**
     * Gibt die Linie b zurück.
     * @return
     */
    public BresenhamLine2d getB() {
    	return this.b;
    }
    
    /**
     *  Übernimmt die Linie für die Linie c.
     * @param c
     */
    public void setC(BresenhamLine2d c) {
    	this.c = c;
    }
    
    /**
     * Gibt die Linie c zurück.
     * @return
     */
    public BresenhamLine2d getC() {
    	return this.c;
    }
    
    /**
     * Überschreibt die Methode <i>toString</i> der Klasse <i>Object</i> und gibt die Werte für die Triangle zurück.
     */
    public String toString() {
    	return "Triangle2d { ax = " + a.x1 + ", ay = " + a.y1 + ", bx = " + b.x1 + ", by = " + b.y1 + ", cx = " + c.x1 + ", cy = " + c.y1;
    }

    /**
     * Rendermethode.
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
    	a.color = color;
    	b.color = color;
    	c.color = color;
        a.render(transformation, camera, renderer);
        b.render(transformation, camera, renderer);
        c.render(transformation, camera, renderer);
    }

    private static final Logger logger = Logger.getLogger(Triangle2d.class.getName());
}
