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
package amcgala.example.gameoflife;

import java.util.Collection;

import amcgala.framework.camera.Camera;
import amcgala.framework.lighting.Light;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.Polygon;
import amcgala.framework.shape.Shape;

public class Element extends Shape {

	private Polygon polygon;
	public boolean isAlive;

	public Element(int width, int height, double x, double y) {
		this.isAlive = false;
		polygon = new Polygon(new Vector3d(x, y, 0), new Vector3d(x + width, y,
				0), new Vector3d(x+width, y + height, 0), new Vector3d(x , y
				+ height, 0), new Vector3d(0, 0, 0));
	}

	@Override
	public void render(Matrix arg0, Camera arg1, Renderer arg2, Collection<Light> lights) {
		if (this.isAlive) {
			polygon.render(arg0, arg1, arg2, lights);
		}
	}

}
