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
 * Klasse für das Spotlight, dient zur Berechnung einer Lichtquelle
 * die in eine bestimmte Richtung innerhalb eines bestimmten Bereiches 
 * Licht abstrahlt das mit Entfernung abnimmt.
 * @author Sascha Lemke
 */
public class SpotLight implements Light {
	
	private String name;
	
	// ambiente elemente
	private AmbientLight ambient;
	
	// pointelemente
	private Vector3d position;
	private double intensity = 1;
	
	// Spotlight werte
	private Vector3d direction;
	private Color color = new Color(255, 255, 255);
	
	// Attenuation
	private double constantAttenuation = 0;
	private double linearAttenuation = 0;
	private double exponentialAttenuation = 1;
	

	public SpotLight(String name, AmbientLight ambient, Vector3d position, Vector3d direction) {
		this.name = name;
		this.ambient = ambient;
		this.position = position;
		this.direction = direction;
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
	 * @return the position
	 */
	public Vector3d getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3d position) {
		this.position = position;
	}

	/**
	 * @return the direction
	 */
	public Vector3d getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

	/**
	 * @return the constantAttenuation
	 */
	public double getConstantAttenuation() {
		return constantAttenuation;
	}

	/**
	 * @param constantAttenuation the constantAttenuation to set
	 */
	public void setConstantAttenuation(double constantAttenuation) {
		this.constantAttenuation = constantAttenuation;
	}

	/**
	 * @return the linearAttenuation
	 */
	public double getLinearAttenuation() {
		return linearAttenuation;
	}

	/**
	 * @param linearAttenuation the linearAttenuation to set
	 */
	public void setLinearAttenuation(double linearAttenuation) {
		this.linearAttenuation = linearAttenuation;
	}

	/**
	 * @return the exponentialAttenuation
	 */
	public double getExponentialAttenuation() {
		return exponentialAttenuation;
	}

	/**
	 * @param exponentialAttenuation the exponentialAttenuation to set
	 */
	public void setExponentialAttenuation(double exponentialAttenuation) {
		this.exponentialAttenuation = exponentialAttenuation;
	}

	@Override
	public Color interpolate(Color color, Vector3d pixelposition, Vector3d camera, Appearance appearance) {
		pixelposition.normalize();
		direction.normalize();
		double angle = this.position.dot(pixelposition);
		double spotFactor = pixelposition.dot(direction);
		double cutOff = Math.cos(spotFactor);
			
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

		if(spotFactor >= cutOff) {
			if(angle > 0) {
		
				/*
				 * Berechnung der Punktlichtintensität.
				 */
				double pointIntensityRed = ((this.color.getR() / 2.55) * this.intensity) / 100;
				double pointIntensityGreen = ((this.color.getG() / 2.55) * this.intensity) / 100;
				double pointIntensityBlue = ((this.color.getB() / 2.55) * this.intensity) / 100;
				
				/*
				 * Berechnung des Austrittsvektors
				 */
				Vector3d rj = pixelposition.times(pixelposition.dot(this.position));
				
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
				Vector3d distanceVector = this.position.sub(pixelposition);
				double distance = Math.sqrt(Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.z, 2));
				
				/*
				 * Berechnung der Abschwächung.
				 */
				double attenuation = Math.min(1,  1 / (this.constantAttenuation + this.linearAttenuation * distance + this.exponentialAttenuation * Math.pow(distance, 2)));
				
				double spotAttenuation = 1.0 - (1.0 - spotFactor) * 1.0 / (1.0 - cutOff);
				
				/*
				 * Berechnung der finalen Farbwerte.
				 */
				float r = (float) ((ambientIntensityRed * reflectionRed) + ( (pointIntensityRed * reflectionRed) * angle + specularRed) * attenuation * spotAttenuation);
				float g = (float) ((ambientIntensityGreen * reflectionGreen) + ( (pointIntensityGreen * reflectionGreen) * angle + specularGreen) * attenuation * spotAttenuation);
				float b = (float) ((ambientIntensityBlue * reflectionBlue) + ( (pointIntensityBlue * reflectionBlue) * angle + specularBlue) * attenuation * spotAttenuation);
				
				/*
				 * Abfangen möglicher Rundungsfehler.
				 */
				if(r > 1) r = 1;
				if(g > 1) g = 1;
				if(b > 1) b = 1;
				
				return new Color(r, g, b);
				
			} else {
				
				/*
				 * ambientes Licht für die Seite die dem Licht nicht zugewandt ist.
				 */
				float r = (float) (ambientIntensityRed * reflectionRed);
				float g = (float) (ambientIntensityGreen * reflectionGreen);
				float b = (float) (ambientIntensityBlue * reflectionBlue);
				
				return new Color(r,g, b);
			}
		} else {
			/*
			 * Berechne ambientes Licht, falls nicht vom spotlight angestrahlt
			 */
			float r = (float) (ambientIntensityRed * reflectionRed);
			float g = (float) (ambientIntensityGreen * reflectionGreen);
			float b = (float) (ambientIntensityBlue * reflectionBlue);
			
			return new Color(r,g, b);
		}
	}
	
}
