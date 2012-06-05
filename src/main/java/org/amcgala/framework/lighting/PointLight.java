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

public class PointLight implements Light {

	// ambientlight variables
	private String name;
	private double ambientIntensity = 0.9;

	private double reflexionskoeffizient = 1; // in appearance packen
	private Color ambientColor = new Color(255, 255, 255);
	
	// pointlight variables
	private Vector3d position;
	private Color pointColor = new Color(255, 255, 255);
	private double pointIntensity = 1;
	private double constantAttenuation = 0;
	private double linearAttenuation = 0;
	private double exponentialAttenuation = 1;
	
	
	/**
	 * Konstruktor.
	 * @param name Der Name der Lichtquelle
	 * @param ambientItensity Die Intensität des ambienten Lichts
	 * @param ambientColor Die Farbe des ambienten Lichts
	 * @param position Die Position des Pointlights
	 * @param pointLightColor Die Farbe des Pointlights
	 */
	public PointLight(String name, double ambientItensity, Color ambientColor, Vector3d position, Color pointLightColor) {
		this.name = name;
		this.ambientIntensity = ambientItensity;
		this.ambientColor = ambientColor;
		this.position = position;
		this.pointColor = pointLightColor;
	}
	
	/**
	 * Konstruktor.
	 * @param name Der Name der Lichtquelle
	 * @param ambient Das Ambientelicht für diese Lichtquelle
	 * @param position Die Position der Lichtquelle
	 * @param pointLightColor Die Farbe der Lichtquelle
	 */
	public PointLight(String name, AmbientLight ambient, Vector3d position, Color pointLightColor) {
		this.name = name;
		this.ambientIntensity = ambient.getIntensity();
		this.ambientColor = ambient.getColor();
		this.position = position;
		this.pointColor = pointLightColor;
	}

	/**
	 * Gibt die Intensität des ambienten Lichts zurück.
	 * @return Die Intensität des ambienten Lichts.
	 */
	public double getAmbientIntensity() {
		return ambientIntensity;
	}

	/**
	 * Setzt die Intensität des ambienten Lichts auf den übergebenen Wert.
	 * @param ambientIntensity Die neue Intensität
	 */
	public void setAmbientIntensity(double ambientIntensity) {
		if(ambientIntensity > 1 || ambientIntensity < 0) {
			throw new IllegalArgumentException();
		} else {
			this.ambientIntensity = ambientIntensity;
		}
	}
	
	/**
	 * Gibt den Reflexionskoeffizient zurück.
	 * @return der Reflexionskoeffizient.
	 */
	public double getReflexionskoeffizient() {
		return reflexionskoeffizient;
	}

	/**
	 * Setzt den Reflexionskoeffizienten auf den übergebenen Wert.
	 * @param reflexionskoeffizient Der Reflexionskoeffizient
	 */
	public void setReflexionskoeffizient(double reflexionskoeffizient) {
		if(reflexionskoeffizient > 1 || reflexionskoeffizient < 0) {
			throw new IllegalArgumentException();
		} else {
			this.reflexionskoeffizient = reflexionskoeffizient;
		}
	}

	/**
	 * Gibt die Position des Pointlights zurück.
	 * @return Die Position des Pointlights
	 */
	public Vector3d getPosition() {
		return position;
	}

	/**
	 * Setzt die Position des Pointlights
	 * @param position Die Position des Pointlights
	 */
	public void setPosition(Vector3d position) {
		this.position = position;
	}

	/**
	 * Gibt die Farbe des Pointlights zurück.
	 * @return Die Farbe
	 */
	public Color getPointColor() {
		return pointColor;
	}

	/**
	 * Setzt die Farbe des Pointlight auf den übergebenen Wert.
	 * @param pointColor Die Farbe
	 */
	public void setPointColor(Color pointColor) {
		this.pointColor = pointColor;
	}

	/**
	 * Gibt die Intensiät des Pointlights zurück.
	 * @return Die Intensität des Pointlights
	 */
	public double getPointIntensity() {
		return pointIntensity;
	}

	/**
	 * Setzt die Intensität des Pointlights auf den übergebenen Wert.
	 * @param pointIntensity Die Intensität des Pointlights
	 */
	public void setPointIntensity(double pointIntensity) {
		if(pointIntensity > 1 || pointIntensity < 0) {
			throw new IllegalArgumentException();
		} else {
			this.pointIntensity = pointIntensity;
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Setzt die Farbe des ambienten Lichts.
	 * @param color Die neue Farbe
	 */
	public void setAmbientColor(Color color) {
		this.ambientColor = color;
	}
	
	/**
	 * Gibt die Farbe des ambienten Lichts zurück.
	 * @return Die Farbe
	 */
	public Color getAmbientColor() {
		return this.ambientColor;
	}
	
	/**
	 * Gibt die Farbe des Pointlights zurück.
	 * @return Die Farbe
	 */
	public Color getColor() {
		return this.pointColor;
	}

	/**
	 * Setzt die Farbe des Pointlights.
	 * @param color Die Farbe
	 */
	public void setColor(Color color) {
		this.pointColor = color;
	}
	
	/**
	 * Gibt die konstante Lichtabschwächung zurück.
	 * @return the constantAttenuation die konstante Lichtabschwächung
	 */
	public double getConstantAttenuation() {
		return constantAttenuation;
	}

	/**
	 * Setzt die konstante Lichtabschächung auf den übergebenen Wert.
	 * @param constantAttenuation the constantAttenuation to set
	 */
	public void setConstantAttenuation(double constantAttenuation) {
		this.constantAttenuation = constantAttenuation;
	}

	/**
	 * Gibt die lineare Lichtabschwächung zurück.
	 * @return the linearAttenuation die lineare Lichtabschwächung
	 */
	public double getLinearAttenuation() {
		return linearAttenuation;
	}

	/**
	 * Setzt die lineare Lichtabschwächung auf den übergebenen Wert.
	 * @param linearAttenuation the linearAttenuation to set
	 */
	public void setLinearAttenuation(double linearAttenuation) {
		this.linearAttenuation = linearAttenuation;
	}

	/**
	 * Gibt die exponentielle Lichtabschwächung zurück.
	 * @return the exponentialAttenuation die exponentielle Lichtabschwächung
	 */
	public double getExponentialAttenuation() {
		return exponentialAttenuation;
	}

	/**
	 * Setzt die exponentielle Lichtabschwächung auf den übergebenen Wert.
	 * @param exponentialAttenuation the exponentialAttenuation to set
	 */
	public void setExponentialAttenuation(double exponentialAttenuation) {
		if(exponentialAttenuation < this.pointIntensity) {
			throw new IllegalArgumentException("Die exponentielle Abschwächung muss größer als die Intensität des Punktlichts sein.");
		} else {
			this.exponentialAttenuation = exponentialAttenuation;
		}
	}

	@Override
	public Color interpolate(Color color, Vector3d n) {
		Vector3d a = n.copy();
		a.normalize();
		double angle = this.position.dot(n);
		
		if(angle > 0) {
			// ambient + diffuse 
			double ambientIntensityRed = ((this.ambientColor.getR() / 2.55) * this.ambientIntensity) / 100;
			double ambientIntensityGreen = ((this.ambientColor.getG() / 2.55) * this.ambientIntensity) / 100;
			double ambientIntensityBlue = ((this.ambientColor.getB() / 2.55) * this.ambientIntensity) / 100;
	
			double reflectionRed = ((color.getR() / 2.55) * this.reflexionskoeffizient) / 100;
			double reflectionGreen = ((color.getG() / 2.55) * this.reflexionskoeffizient) / 100;
			double reflectionBlue = ((color.getB() / 2.55) * this.reflexionskoeffizient) / 100;
	
			double pointIntensityRed = ((this.pointColor.getR() / 2.55) * this.pointIntensity) / 100;
			double pointIntensityGreen = ((this.pointColor.getG() / 2.55) * this.pointIntensity) / 100;
			double pointIntensityBlue = ((this.pointColor.getB() / 2.55) * this.pointIntensity) / 100;
			
			
			Vector3d distance = this.position.sub(a);
			double d = Math.sqrt(Math.pow(distance.x, 2) + Math.pow(distance.x, 2) + Math.pow(distance.z, 2));
			
			double c = Math.min(1.0, 1 / (this.constantAttenuation + this.linearAttenuation * d + this.exponentialAttenuation * Math.pow(d, 2)));

			float r = (float) ((ambientIntensityRed * reflectionRed + pointIntensityRed * reflectionRed * angle * c));
			
			float g = (float) ((ambientIntensityGreen * reflectionGreen + pointIntensityGreen * reflectionGreen * angle * c));
			
			float b = (float) ((ambientIntensityBlue * reflectionBlue + pointIntensityBlue * reflectionBlue * angle * c));
			//System.out.println(r);
			return new Color( r,  g,  b);
			
		} else {
			double ambientIntensityRed = ((this.ambientColor.getR() / 2.55) * this.ambientIntensity) / 100;
			double ambientIntensityGreen = ((this.ambientColor.getG() / 2.55) * this.ambientIntensity) / 100;
			double ambientIntensityBlue = ((this.ambientColor.getB() / 2.55) * this.ambientIntensity) / 100;
			
			double reflectionRed = ((color.getR() / 2.55) * this.reflexionskoeffizient) / 100;
			double reflectionGreen = ((color.getG() / 2.55) * this.reflexionskoeffizient) / 100;
			double reflectionBlue = ((color.getB() / 2.55) * this.reflexionskoeffizient) / 100;
			
			float r = (float) (ambientIntensityRed * reflectionRed);
			float g = (float) (ambientIntensityGreen * reflectionGreen);
			float b = (float) (ambientIntensityBlue * reflectionBlue);
			
			return new Color(r,g,b);
		}
	}

	/**
	 * Gibt die Werte des Pointlights als String aus.
	 */
	public String toString() {
		String output = "";
		output += "Punktlicht: " + this.name;
		output += " { ambiente Intensität: " + this.ambientIntensity + "; ";
		output += "Reflexionskoeffizient: " + this.reflexionskoeffizient + "; ";
		output += "ambiente Farbe: " + this.ambientColor.toString() + "; ";
		output += "Position: " + this.position.toString() + " ";
		output += "Farbe des Punktlichts" + this.pointColor.toString() + "; ";
		return output;
	}
}
