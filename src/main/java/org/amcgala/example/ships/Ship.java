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

import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Line;
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
    private Line l1, l2;

    public Ship(double width, double height, double x, double y, Heading heading) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.heading = heading;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new Line(x, y, x, y + height);
            l2 = new Line(x + width, y, x + width, y + height);
        } else {
            l1 = new Line(x, y, x + width, y);
            l2 = new Line(x, y + height, x + width, y + height);
        }
    }

    public void setX(double x) {
        this.x = x;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new Line(x, y, x, y + height);
            l2 = new Line(x + width, y, x + width, y + height);
        } else {
            l1 = new Line(x, y, x + width, y);
            l2 = new Line(x, y + height, x + width, y + height);
        }
    }

    public void setY(double y) {
        this.y = y;
        if (Heading.BOTTOM.equals(heading) || Heading.TOP.equals(heading)) {
            l1 = new Line(x, y, x, y + height);
            l2 = new Line(x + width, y, x + width, y + height);
        } else {
            l1 = new Line(x, y, x + width, y);
            l2 = new Line(x, y + height, x + width, y + height);
        }
    }

    @Override
    public void render(Renderer renderer) {
        l1.render(renderer);
        l2.render(renderer);
    }
}
