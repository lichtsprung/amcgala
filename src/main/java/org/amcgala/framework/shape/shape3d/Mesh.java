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

package org.amcgala.framework.shape.shape3d;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.util.CompositeShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Ein gefülltes {@link org.amcgala.framework.shape.Shape} das aus Polygonen besteht.
 *
 * @author Sascha Lemke
 * @author Robert Giacinto
 */
public class Mesh extends AbstractShape {

    private static final Logger log = LoggerFactory.getLogger(Mesh.class.getName());
    private CompositeShape c;

    public Mesh(Vector3d position, int width, int height, int depth) {
        c = new CompositeShape();
        // rechts
        for (int i = 0; i < height; i++) {
            c.add(new Line(new Vector3d(position.x, position.y + i, position.z), new Vector3d(width, position.y + i, position.z)));
        }
        // hinten
        for (int i = 0; i < height; i++) {
            c.add(new Line(new Vector3d(position.x, position.y + i, position.z), new Vector3d(position.x, position.y + i, depth)));
        }
        // vorn
        for (int i = 0; i < height; i++) {
            c.add(new Line(new Vector3d(width, position.y + i, position.z), new Vector3d(width, position.y + i, depth)));
        }
        // links
        for (int i = 0; i < height; i++) {
            c.add(new Line(new Vector3d(position.x, position.y + i, depth), new Vector3d(width, position.y + i, depth)));
        }
        // oben
        for (int i = 0; i < depth; i++) {
            c.add(new Line(new Vector3d(position.x, position.y + height, i), new Vector3d(width, position.y + height, i)));
        }
        // unten
        for (int i = 0; i < depth; i++) {
            c.add(new Line(new Vector3d(position.x, position.y, i), new Vector3d(width, position.y, i)));
        }
    }


    @Override
    public void render(Renderer renderer) {
        c.render(renderer);
    }
}