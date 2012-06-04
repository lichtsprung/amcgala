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
package org.amcgala.framework.shape.shape2d;

import java.util.Collection;

import org.amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.shape.BresenhamLine;
import org.amcgala.framework.shape.Shape;

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
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        a.render(transformation, camera, renderer, lights);
        b.render(transformation, camera, renderer, lights);
        c.render(transformation, camera, renderer, lights);
    }
    
    private static final Logger log = LoggerFactory.getLogger(Triangle.class.getName());
}
