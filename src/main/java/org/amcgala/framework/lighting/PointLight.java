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
 * Klasse für das Punktlicht, dient zur Berechnung einer Lichtquelle die in 
 * alle Richtungen von einem bestimmten Punkt aus Licht abstrahlt und das 
 * mit der Entfernung schwächer wird.
 * @author Sascha Lemke
 */
public class PointLight implements Light {

	// ambientlight variables
	private String name;
	private AmbientLight ambient;
	
	// pointlight variables
	private Vector3d position;
	private Color pointColor = new Color(255, 255, 255);
	private double pointIntensity = 1;
	private double constantAttenuation = 0;
	private double linearAttenuation = 0;
	private double exponentialAttenuation = 1;
	
	/**
	 * QuickKonstruktor, erstellt ein Licht mit den Basiseinstellungen und möglichst wenig Parametern.
	 * @param name Der Name der Lichtquelle
	 * @param ambient Das ambiente Licht
	 * @param position Die Position der Lichtquelle
	 */
	public PointLight(String name, AmbientLight ambient, Vector3d position) {
		this.name = name;
		this.ambient = ambient; // muss nicht geprüft werde, da dies schon beim ambienten Licht passiert.
		this.position = position;
	}
	
	/**
	 * Konstruktor.
	 * @param name Der Name der Lichtquelle
	 * @param ambientItensity Die Intensität des ambienten Lichts
	 * @param ambientColor Die Farbe des ambienten Lichts
	 * @param position Die Position des Pointlights
	 * @param pointLightColor Die Farbe des Pointlights
	 */
	public PointLight(String name, double ambientIntensity, Color ambientColor, Vector3d position, Color pointLightColor) {
		this.name = name;
		if(ambientIntensity > 1 || ambientIntensity < 0) {
			throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
		} else {
			this.ambient.setIntensity(ambientIntensity);
		}
		this.ambient.setColor(ambientColor);
		this.position = position;
		this.pointColor = pointLightColor;
	}

	/**
	 * Gibt die Intensität des ambienten Lichts zurück.
	 * @return Die Intensität des ambienten Lichts.
	 */
	public double getAmbientIntensity() {
		return ambient.getIntensity();
	}

	/**
	 * Setzt die Intensität des ambienten Lichts auf den übergebenen Wert.
	 * @param ambientIntensity Die neue Intensität
	 */
	public void setAmbientIntensity(double ambientIntensity) {
		if(ambientIntensity > 1 || ambientIntensity < 0) {
			throw new IllegalArgumentException("Die ambiente Intensität muss zwischen 0.0 und 1.0 liegen!");
		} else {
			this.ambient.setIntensity(ambientIntensity);
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
		this.ambient.setColor(color);
	}
	
	/**
	 * Gibt die Farbe des ambienten Lichts zurück.
	 * @return Die Farbe
	 */
	public Color getAmbientColor() {
		return this.ambient.getColor();
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
			this.exponentialAttenuation = exponentialAttenuation;
	}

	@Override
	public Color interpolate(Color color, Vector3d oberflaechennormale, Vector3d camera, Appearance appearance) {
		
		Vector3d normiert = oberflaechennormale.copy();
		normiert.normalize();
		double angle = this.position.dot(oberflaechennormale);
		
		/*
		 * Berechnung der ambienten Intensität.
		 */
		double ambientIntensityRed = ((this.ambient.getColor().getR() / 2.55) * this.ambient.getIntensity()) / 100;
		double ambientIntensityGreen = ((this.ambient.getColor().getG() / 2.55) * this.ambient.getIntensity()) / 100;
		double ambientIntensityBlue = ((this.ambient.getColor().getB() / 2.55) * this.ambient.getIntensity()) / 100;

		/*
		 * Berechnung der Reflexion.
		 */
		double reflectionRed = ((color.getR() / 2.55) * appearance.getReflectionCoefficient()) / 100;
		double reflectionGreen = ((color.getG() / 2.55) * appearance.getReflectionCoefficient()) / 100;
		double reflectionBlue = ((color.getB() / 2.55) * appearance.getReflectionCoefficient()) / 100;
		
		if(angle > 0) {
	
			/*
			 * Berechnung der Punktlichtintensität.
			 */
			double pointIntensityRed = ((this.pointColor.getR() / 2.55) * this.pointIntensity) / 100;
			double pointIntensityGreen = ((this.pointColor.getG() / 2.55) * this.pointIntensity) / 100;
			double pointIntensityBlue = ((this.pointColor.getB() / 2.55) * this.pointIntensity) / 100;
			
			/*
			 * Berechnung des Austrittsvektors
			 */
			Vector3d rj = normiert.times(normiert.dot(this.position));
			rj.times(2);
			
			/*
			 * Berechnung der Spiegelreflexion 
			 */
			double result = Math.pow(rj.dot(camera), appearance.getSpecularExponent());
			
			double specularRed = pointIntensityRed * appearance.getSpecularCoefficient() * result;
			double specularGreen = pointIntensityGreen * appearance.getSpecularCoefficient() * result;
			double specularBlue = pointIntensityBlue * appearance.getSpecularCoefficient() * result;
			
			/*
			 * Berechnung der Distanz von dem Pixel zur Lichtquelle.
			 */
			Vector3d distanceVector = this.position.sub(normiert);
			double distance = Math.sqrt(Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.z, 2));
			
			/*
			 * Berechnung der Abschwächung.
			 */
			double attenuation = Math.min(1,  1 / (this.constantAttenuation + this.linearAttenuation * distance + this.exponentialAttenuation * Math.pow(distance, 2)));
			
			/*
			 * Berechnung der finalen Farbwerte.
			 */
			float r = (float) ((ambientIntensityRed * reflectionRed) + ( (pointIntensityRed * reflectionRed) * angle + specularRed) * attenuation );
			float g = (float) ((ambientIntensityGreen * reflectionGreen) + ( (pointIntensityGreen * reflectionGreen) * angle + specularGreen) * attenuation);
			float b = (float) ((ambientIntensityBlue * reflectionBlue) + + ( (pointIntensityBlue * reflectionBlue) * angle + specularBlue) * attenuation);
			
			/*
			 * Abfangen möglicher Rundungsfehler.
			 */
			if(r > 1) r = 1;
			if(g > 1) g = 1;
			if(b > 1) b = 1;
			
			return new Color(r, g, b);
			
		} else {
			
			float r = (float) (ambientIntensityRed * reflectionRed);
			float g = (float) (ambientIntensityGreen * reflectionGreen);
			float b = (float) (ambientIntensityBlue * reflectionBlue);
			
			return new Color(r,g, b);
		}
	}

	/**
	 * Gibt die Werte des Pointlights als String aus.
	 * @return Die Werte des Pointlights als String
	 */
	public String toString() {
		String output = "";
		output += "Punktlicht: " + this.name;
		output += " { \n";
		output += "\t ambiente Intensität: " + this.ambient.getIntensity() + "; \n";
		output += "\t ambiente Farbe: " + this.ambient.getColor().toString() + "; \n";
		output += "\t Position: " + this.position.toString() + " \n";
		output += "\t Farbe des Punktlichts: " + this.pointColor.toString() + "; \n";
		output += "\t Intensität des Punktlichts: " + this.pointIntensity + "; \n";
		output += "\t Konstante Abschwächung: " + this.constantAttenuation + "; \n";
		output += "\t Lineare Abschwächung: " + this.linearAttenuation + "; \n";
		output += "\t Exponentielle Abschwächung: " + this.exponentialAttenuation + "; \n}";
		return output;
	}
}
