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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Pixel;

import amcgala.framework.lighting.Light;
import amcgala.framework.renderer.Renderer;

/**
 * Eine Linie im 3d Raum.
 *
 * @author Robert Giacinto
 */
public class BresenhamLine extends Shape {

    public double x1, y1, z1;
    public double x2, y2, z2;
    private Vector3d start, end;
    
    public BresenhamLine(double x1, double y1, double x2, double y2) {
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

        start = new Vector3d(x1, y1, -1);
        end = new Vector3d(x2, y2, -1);
    }

    public BresenhamLine(Vector3d start, Vector3d end) {
        this.x1 = start.x;
        this.y1 = start.y;
        this.z1 = start.z;
        this.x2 = end.x;
        this.y2 = end.y;
        this.z2 = end.z;
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        // Einbeziehen der Transformationsgruppen. Um Animationen zu beruecksichtigen, die auf die einzelnen Felder zugegriffen
        // haben, werden die start und end Vektoren aktualisiert, bevor sie mit der Transformationsmatrix multipliziert werden.
        start = new Vector3d(x1, y1, z1).transform(transformation);
        end = new Vector3d(x2, y2, z2).transform(transformation);

        // Start- und Endpunkt der Linie in Pixeln, mit denen die Linienalgorithmen durchgefuehrt werden.
        Pixel startPixel = camera.getImageSpaceCoordinates(start);
        Pixel endPixel = camera.getImageSpaceCoordinates(end);

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
        
        int i = 1;
        Vector3d v = new Vector3d(-x, -y, -start.z);
        renderer.putPixel(v, color, lights);

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
                v = new Vector3d(-x, -y, -z);
                
                renderer.putPixel(v, color, lights);
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
                v = new Vector3d(-x, -y, -z);
                renderer.putPixel(v, color, lights);
            }
        }

        /*
         * Ende Bresenham Algorithmus
         */


    }
    
    /**
     * toString method.
     */
    public String toString() {
		return "BresenhamLine { x1: " + this.x1 + ", \ty1: " + this.y1 + ", \tx2: " + this.x2 + ", \ty2: " + this.y2 + " }";
    }
    
    private static final Logger log = LoggerFactory.getLogger(BresenhamLine.class.getName());
}
