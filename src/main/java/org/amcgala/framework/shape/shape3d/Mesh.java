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

import java.util.Collection;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.BresenhamLine;
import org.amcgala.framework.shape.Container;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Ein gefülltes "Objekt" das aus Polygonen besteht.
 * @author Sascha Lemke
 *
 */
public class Mesh extends Shape {
	
	private Container c;
	
	public Mesh(Vector3d position, int width, int height, int depth) {
		c = new Container();
		// rechts
		for(int i = 0; i < height; i++) {
			c.add(new BresenhamLine(new Vector3d(position.x, position.y + i, position.z), new Vector3d(width, position.y + i, position.z)));
		}
		// hinten
		for(int i = 0; i < height; i++) {
			c.add(new BresenhamLine(new Vector3d(position.x, position.y + i, position.z), new Vector3d(position.x, position.y + i, depth)));
		}
		// vorn
		for(int i = 0; i < height; i++) {
			c.add(new BresenhamLine(new Vector3d(width, position.y + i, position.z), new Vector3d(width, position.y + i, depth)));
		}
		// links
		for(int i = 0; i < height; i++) {
			c.add(new BresenhamLine(new Vector3d(position.x, position.y + i, depth), new Vector3d(width, position.y + i, depth)));
		}
		// oben
		for(int i = 0; i < depth; i++) {
			c.add(new BresenhamLine(new Vector3d(position.x, position.y + height, i), new Vector3d(width, position.y + height,i)));
		}
		// unten
		for(int i = 0; i < depth; i++) {
			c.add(new BresenhamLine(new Vector3d(position.x, position.y, i), new Vector3d(width,position.y,i)));
		}
	}

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
		c.color = color;
		c.render(transformation, camera, renderer, lights);
	}

    private static final Logger log = LoggerFactory.getLogger(Mesh.class.getName());
}
