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
	
	// Spotlight werte
	private Vector3d direction;
	private double spotIntensity;
	
	// Attenuation
	private double constantAttenuation = 1;
	private double linearAttenuation = 0;
	private double exponentialAttenuation = 0.4;
	
	/**
	 * Konstruktor.
	 */
	public SpotLight() {
		
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
	 * @return the spotIntensity
	 */
	public double getSpotIntensity() {
		return spotIntensity;
	}

	/**
	 * @param spotIntensity the spotIntensity to set
	 */
	public void setSpotIntensity(double spotIntensity) {
		this.spotIntensity = spotIntensity;
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
	public Color interpolate(Color color, Vector3d pixelposition, Vector3d camera, Appearance app) {
		// mächtige interpolate methode für das spotlight
		
		return new Color(0, 0 ,0);
	}

}
