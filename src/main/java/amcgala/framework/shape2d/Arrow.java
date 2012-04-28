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
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Color;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;
import amcgala.framework.shape.Shape;

/**
* Draws a 2D Arrow.
*
* @author Robert Giacinto
*/
public class Arrow extends Shape {

    private Vector3d position;
    private Vector3d direction;
    private double length;
    private BresenhamLine l1;

    /**
	 * Constructor.
	 * @param position
	 * @param direction
	 * @param length
	*/
    public Arrow(Vector3d position, Vector3d direction, double length) {
        this.position = position;
        this.length = length;
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
	 * Returns the direction in form of a 3D Vector.
	 */
    public Vector3d getDirection() {
        return direction;
    }

    /**
	 * Sets the direction.
	 * @param direction
	 */
    public void setDirection(Vector3d direction) {
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
	 * Returns the length.
	 * @return
	 */
    public double getLength() {
        return length;
    }

    /**
	 * Sets the length.
	 * @param length
	 */
    public void setLength(double length) {
        this.length = length;
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
	 * Returns the current position in form of a 3D Vector.
	 * @return
	 */
    public Vector3d getPosition() {
        return position;
    }

    /**
	 * Sets the Position.
	 * @param position
	 */
    public void setPosition(Vector3d position) {
        this.position = position;
        init();
    }

    /**
	 * Rendermethod.
	 */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        l1.render(transformation, camera, renderer, lights);
    }

    /*
	 * Updates the shape at runtime.
	 */
    private void init() {
        l1 = new BresenhamLine(position.x, position.y, position.x + direction.x, position.y + direction.y);
        l1.color = Color.RED;
    }
    
    private static final Logger log = LoggerFactory.getLogger(Arrow.class.getName());
}