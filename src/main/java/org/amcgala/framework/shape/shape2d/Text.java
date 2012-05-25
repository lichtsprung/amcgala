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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;

/**
 * Eine Klasse, die Text darstellen kann. Die grundlegenden Zeichen werden
 * unterstützt und als Vektorbuchstaben ausgegeben.
 * 
 * @author Robert Giacinto, Steffen Troester
 */
public class Text extends Shape {

	private static final double SPACING = 14;
	private double x;
	private double y;
	private Letter[] letters;

	/**
	 * Erzeugt ein Text-Shape, das den Text an der Position (x,y) darstellt.
	 * Dabei wird ein Zeichen als Sprite mit 16x16 Pixel gezaehlt.
	 * 
	 * @param text
	 *            der Text
	 * @param x
	 *            die x-Koordinate der Position
	 * @param y
	 *            die y-Kootdinate der Position
	 */
	public Text(String text, double x, double y) {
		this.x = x;
		this.y = y;
		this.letters = new Letter[text.length()];
		for (int i = 0; i < letters.length; i++) {
			char c = text.charAt(i);
			letters[i] = new Letter(x + i * SPACING, y, c);
		}
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
		for (Shape s : letters) {
			s.render(transformation, camera, renderer, lights);
		}
	}
	
    private static final Logger log = LoggerFactory.getLogger(Text.class.getName());
}
