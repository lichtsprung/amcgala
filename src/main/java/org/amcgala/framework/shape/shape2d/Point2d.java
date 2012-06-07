
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

import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.renderer.Pixel;

/**
* Ein Punkt in einer Ebene für die Darstellung von 2d Geometrien.
* <p/>
*
* @author Robert Giacinto
*/
public class Point2d extends Shape {

	public double x;
	public double y;
	
	public Point2d(double x, double y, Color c) {
		this(x, y);
		this.color = c;
	}
	
	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
		Vector3d point = new Vector3d(x, y, -1).transform(transformation);
		
		Pixel pixel = camera.getImageSpaceCoordinates(point);
		renderer.putPixel(pixel, color);
	}
	
	@Override
	public String toString() {
		return "Point2d{" + "x=" + x + ", y=" + y + '}';
	}

    private static final Logger log = LoggerFactory.getLogger(Point2d.class.getName());
}
