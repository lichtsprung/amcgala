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
package org.amcgala.example.ships;

import java.util.Collection;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.BresenhamLine;
import org.amcgala.framework.shape.Shape;

/**
 * Ein Teil eines Schiffs.
 *
 * @author Robert Giacinto
 */
public class Ship extends Shape {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected Heading heading;
    private BresenhamLine l1, l2;

    public Ship(double width, double height, double x, double y, Heading heading) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.heading = heading;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new BresenhamLine(x, y, x, y + height);
            l2 = new BresenhamLine(x + width, y, x + width, y + height);
        } else {
            l1 = new BresenhamLine(x, y, x + width, y);
            l2 = new BresenhamLine(x, y + height, x + width, y + height);
        }
    }

    public void setX(double x) {
        this.x = x;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new BresenhamLine(x, y, x, y + height);
            l2 = new BresenhamLine(x + width, y, x + width, y + height);
        } else {
            l1 = new BresenhamLine(x, y, x + width, y);
            l2 = new BresenhamLine(x, y + height, x + width, y + height);
        }
    }

    public void setY(double y) {
        this.y = y;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new BresenhamLine(x, y, x, y + height);
            l2 = new BresenhamLine(x + width, y, x + width, y + height);
        } else {
            l1 = new BresenhamLine(x, y, x + width, y);
            l2 = new BresenhamLine(x, y + height, x + width, y + height);
        }
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        l1.render(transformation, camera, renderer, lights);
        l2.render(transformation, camera, renderer, lights);
    }
}
