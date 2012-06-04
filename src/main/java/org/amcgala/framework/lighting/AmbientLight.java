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
package org.amcgala.framework.lighting;

import org.amcgala.framework.math.Vector3d;

import org.amcgala.framework.renderer.Color;

public class AmbientLight implements Light {

	private String name;
	private double intensity = 0.9;
	private double reflexionskoeffizient = 0.9; // in appearance packen
	private Color lightcolor = new Color(255, 255, 255);
	
	/**
	 * Konstruktor für das Ambientlight.
	 * @param name Der Name der Lichtquelle
	 * @param intensity Die Intensität der Lichtquelle
	 * @param reflexion Die Reflexionsstärke des Objektes
	 */
	public AmbientLight(String name, double intensity, Color color) {
		//TODO: Werte prüfen!
		this.name = name;
		this.intensity = intensity;
		this.lightcolor = color;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.lightcolor = color;
	}

	/**
	 * 
	 * @return
	 */
	public Color getColor() {
		return this.lightcolor;
	}

	/**
	 * Sets the intensity for this ambientlight.
	 * @param intensity
	 */
	public void setIntensity(double intensity) {
		if(intensity >= 1) {
			this.intensity = 1.0;
		} else if(intensity >= 0) {
			this.intensity = 0;
		} else {
			this.intensity = intensity;
		}
	}
	
	/**
	 * Returns the intensity for the ambientlight.
	 * @return
	 */
	public double getIntensity() {
		return this.intensity;
	}
	
	/**
	 * 
	 */
	@Override
	public Color interpolate(Color color, Vector3d pixelposition) {
		// die position des pixels wird hier nicht benötigt
	
		double intensityRed = ((this.lightcolor.getR() / 2.55) * this.intensity) / 100;
		double intensityGreen = ((this.lightcolor.getG() / 2.55) * this.intensity) / 100;
		double intensityBlue = ((this.lightcolor.getB() / 2.55) * this.intensity) / 100;

		double reflectionRed = ((color.getR() / 2.55) * this.reflexionskoeffizient) / 100;
		double reflectionGreen = ((color.getG() / 2.55) * this.reflexionskoeffizient) / 100;
		double reflectionBlue = ((color.getB() / 2.55) * this.reflexionskoeffizient) / 100;

		int r = (int) (color.getR() * intensityRed * reflectionRed);
		int g = (int) (color.getG() * intensityGreen * reflectionGreen);
		int b = (int) (color.getB() * intensityBlue * reflectionBlue);
		
		// returns the color for the pixel
		return new Color(r,g,b);
	}
	
	public String toString() {
		return "AmbientLight: " + this.name + " { intensity: " + this.intensity + "; " + this.lightcolor.toString() + " }";
	}
}
