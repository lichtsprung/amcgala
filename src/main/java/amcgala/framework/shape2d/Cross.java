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

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;
import amcgala.framework.shape.Shape;

import java.util.logging.Logger;

/**
 * Ein Kreuz, das einen Punkt markiert.
 *
 * @author Robert Giacinto
 */
public class Cross extends Shape {

    private Vector3d position;
    private int size;
    private BresenhamLine l1, l2;

    /**
     * @param position
     * @param size
     */
    public Cross(Vector3d position, int size) {
        this.position = position;
        this.size = size;
        init();
    }

    /**
     * @param x
     * @param y
     * @param size
     */
    public Cross(double x, double y, int size) {
        this.position = new Vector3d(x, y, -1);
        this.size = size;
        init();
    }

    /**
     * Gibt die Position in Form eines 3D Vektors zurück.
     *
     * @return
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * @param position
     */
    public void setPosition(Vector3d position) {
        this.position = position;
        init();
    }

    /**
     * Gibt die Größe des Kreuzes zurück.
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Setzt die Größe des Kreuzes auf den übergebenen Wert.
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
        init();
    }

    /*
     * 
     */
    private void init() {
        l1 = new BresenhamLine(position.x - size, position.y - size, position.x + size, position.y + size);
        l2 = new BresenhamLine(position.x - size, position.y + size, position.x + size, position.y - size);
    }

    /**
     *
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        l1.color = color;
        l2.color = color;
        l1.render(transformation, camera, renderer);
        l2.render(transformation, camera, renderer);
    }

    private static final Logger LOG = Logger.getLogger(Cross.class.getName());
}
