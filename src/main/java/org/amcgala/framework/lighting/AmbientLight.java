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

import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.math.Vector3d;

import org.amcgala.framework.renderer.Color;

/**
 * Klasse zur Berechnung des ambienten Lichts.
 * Ambientes Licht fällt von allen Richtungen gleichstark auf ein Objekt.
 * @author Sascha Lemke
 */
public class AmbientLight implements Light {

	private String name = "AmbientLight";
	private double intensity = 1;
	private Color lightcolor = new Color(255, 255, 255);
	
	/**
	 * Konstruktor für das Ambientlight.
	 * @param name Der Name der Lichtquelle
	 * @param intensity Die Intensität der Lichtquelle
	 * @param reflexion Die Reflexionsstärke des Objektes
	 */
	public AmbientLight(String name, double intensity, Color color) {
		this.name = name;
		if(intensity > 1 || intensity < 0) {
			throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
		} else {
			this.intensity = intensity;
		}
		this.lightcolor = color;
	}

	/*
	 * (non-Javadoc)
	 * @see org.amcgala.framework.lighting.Light#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.amcgala.framework.lighting.Light#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Setzt die Farbe des ambienten Lichts.
	 * @param color die neue Farbe
	 */
	@Override
	public void setColor(Color color) {
		this.lightcolor = color;
	}

	/**
	 * Gibt die Farbe des ambienten Lichts zurück.
	 * @return die Farbe
	 */
	@Override
	public Color getColor() {
		return this.lightcolor;
	}

	/**
	 * Sets the intensity for this ambientlight.
	 * @param intensity die intensität
	 */
	public void setIntensity(double intensity) {
		if(intensity > 1 || intensity < 0) {
			throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
		} else {
			this.intensity = intensity;
		}
	}
	
	/**
	 * Returns the intensity for the ambientlight.
	 * @return die Intensität
	 */
	public double getIntensity() {
		return this.intensity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.amcgala.framework.lighting.Light#interpolate(org.amcgala.framework.renderer.Color, org.amcgala.framework.math.Vector3d, org.amcgala.framework.math.Vector3d, org.amcgala.framework.appearance.Appearance)
	 */
	@Override
	public Color interpolate(Color color, Vector3d pixelposition, Vector3d camera, Appearance app) {
		/*
		 * Berechnung des ambienten Lichts, die pixelposition wird hier nicht benötigt.
		 * 
		 * Berechnet die Intensität der Farbkanäle.
		 */
		double intensityRed = ((this.lightcolor.getR() / 2.55) * this.intensity) / 100;
		double intensityGreen = ((this.lightcolor.getG() / 2.55) * this.intensity) / 100;
		double intensityBlue = ((this.lightcolor.getB() / 2.55) * this.intensity) / 100;

		/*
		 * Berechnung des Reflexionskoeffzienten.
		 */
		double reflectionRed = ((color.getR() / 2.55) * app.getReflectionCoefficient()) / 100;
		double reflectionGreen = ((color.getG() / 2.55) * app.getReflectionCoefficient()) / 100;
		double reflectionBlue = ((color.getB() / 2.55) * app.getReflectionCoefficient()) / 100;

		/*
		 * Berechnung der finalen Werte für die Farbkanäle.
		 */
		float r = (float) (intensityRed * reflectionRed);
		float g = (float) (intensityGreen * reflectionGreen);
		float b = (float) (intensityBlue * reflectionBlue);
		
		return new Color(r,g,b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "AmbientLight: " + this.name + " { intensity: " + this.intensity + "; " + this.lightcolor.toString() + " }";
	}
}
