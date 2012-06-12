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
package org.amcgala.example.pong;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;


/**
 * Draws a 2D Arrow.
 *
 * @author Robert Giacinto
 */
public class Arrow extends Shape {

    private Vector3d position;
    private Vector3d direction;
    private double length;
    private Line l1;

    /**
     * Constructor.
     *
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
     *
     * @param direction
     */
    public void setDirection(Vector3d direction) {
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
     * Returns the length.
     *
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * Sets the length.
     *
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
        this.direction = direction.normalize().times(length);
        init();
    }

    /**
     * Returns the current position in form of a 3D Vector.
     *
     * @return
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Sets the Position.
     *
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
    public void render(Renderer renderer) {
        l1.render(renderer);
    }

    /*
     * Updates the shape at runtime.
     */
    private void init() {
        l1 = new Line(position.x, position.y, position.x + direction.x, position.y + direction.y);
        l1.setColor(Color.RED);
    }

    private static final Logger log = LoggerFactory.getLogger(Arrow.class.getName());
}
