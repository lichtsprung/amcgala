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
package amcgala.framework.shape.shape3d;

import java.util.Collection;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.shape.Container;
import amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amcgala.framework.lighting.Light;
import amcgala.framework.renderer.Color;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;


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
			BresenhamLine b = new BresenhamLine(new Vector3d(position.x, position.y + i, position.z), new Vector3d(width, position.y + i, position.z));
			b.color = new Color(0, 0, 0);
			c.add(b);
		}
		// hinten
		for(int i = 0; i < height; i++) {
			BresenhamLine b = new BresenhamLine(new Vector3d(position.x, position.y + i, position.z), new Vector3d(position.x, position.y + i, depth));
			b.color = new Color(0, 0, 0);
			c.add(b);
		}
		// vorn
		for(int i = 0; i < height; i++) {
			BresenhamLine b = new BresenhamLine(new Vector3d(width, position.y + i, position.z), new Vector3d(width, position.y + i, depth));
			b.color = new Color(0, 0, 0);
			c.add(b);
		}
		// links
		for(int i = 0; i < height; i++) {
			BresenhamLine b = new BresenhamLine(new Vector3d(position.x, position.y + i, depth), new Vector3d(width, position.y + i, depth));
			b.color = new Color(0, 0, 0);
			c.add(b);
			
		}
		// oben
		for(int i = 0; i < depth; i++) {
			BresenhamLine b = new BresenhamLine(new Vector3d(position.x, position.y + height, i), new Vector3d(width, position.y + height,i));
			b.color = new Color(0 ,0 ,0);
			c.add(b);
		}
		// unten
		for(int i = 0; i < depth; i++) {
			BresenhamLine b = new BresenhamLine(new Vector3d(position.x, position.y, i), new Vector3d(width,position.y,i));
			b.color = new Color(0, 0, 0);
			c.add(b);
		}
	}

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
		c.render(transformation, camera, renderer, lights);
	}

    private static final Logger log = LoggerFactory.getLogger(Mesh.class.getName());
}
