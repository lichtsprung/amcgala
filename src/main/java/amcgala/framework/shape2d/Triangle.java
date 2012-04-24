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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;
import amcgala.framework.shape.Shape;

public class Triangle extends Shape {

    public BresenhamLine a, b, c;

    public Triangle(BresenhamLine a, BresenhamLine b, BresenhamLine c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle(double ax, double ay, double bx, double by, double cx, double cy) {
        a = new BresenhamLine(cx, cy, bx, by);
        b = new BresenhamLine(ax, ay, cx, cy);
        c = new BresenhamLine(ax, ay, bx, by);
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        a.render(transformation, camera, renderer);
        b.render(transformation, camera, renderer);
        c.render(transformation, camera, renderer);
    }
    
    private static final Logger log = LoggerFactory.getLogger(Triangle.class.getName());
}
