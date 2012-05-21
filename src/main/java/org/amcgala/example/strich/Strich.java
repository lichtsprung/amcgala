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
package org.amcgala.example.strich;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.BresenhamLine;
import org.amcgala.framework.shape.Shape;

/**
 * Ein 2d-Strich zwischen zwei Punkten.
 *
 * @author Robert Giacinto
 */
public class Strich extends Shape {

    private BresenhamLine line;

    public Strich(int x1, int y1, int x2, int y2) {
        line = new BresenhamLine(x1, y1, x2, y2);
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        line.render(transformation, camera, renderer);
    }
}
