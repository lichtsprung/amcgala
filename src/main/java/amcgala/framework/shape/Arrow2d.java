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
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;

/**
 * Ein 2d Pfeil.
 * @author Robert Giacinto
 */
public class Arrow2d extends Shape {

    private Vector3d position;
    private Vector3d direction;
    private double length;
    private BresenhamLine2d l1;

    /**
     * 
     * @param position
     * @param direction
     * @param length
     */
    public Arrow2d(Vector3d position, Vector3d direction, double length) {
        this.position = position;
        this.length = length;
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
     * Gibt die Richtung des Pfeils zurück.
     * @return
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * Setzt die Richtung des Pfeils.
     * @param direction
     */
    public void setDirection(Vector3d direction) {
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
     * Gibt die Länge des Pfeils zurück.
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * Setzt die Länge des Pfeils
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
     * Gibt die Position zurück.
     * @return
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Setzt die Position des Pfeils.
     * @param position
     */
    public void setPosition(Vector3d position) {
        this.position = position;
        init();
    }

    /**
     * Rendermethode.
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        l1.color = color;
        l1.render(transformation, camera, renderer);
    }

    /*
     * init methode zur neuberechnung.
     */
    private void init() {
        l1 = new BresenhamLine2d(position.x, position.y, position.x + direction.x, position.y + direction.y);
    }
    
    private static final Logger logger = Logger.getLogger(Arrow2d.class.getName());
}
