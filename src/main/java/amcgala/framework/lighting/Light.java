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
package amcgala.framework.lighting;

import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Color;

/**
 * Interface für Lichter.
 * @author Sascha Lemke
 */
public interface Light {

	/**
	 * Gibt den Namen des Lichts zurück
	 * @return der Name des Lichts
	 */
	public String getName();
	
	/**
	 * Setzt den Namen des Lichts auf den übergebenen Wert.
	 * @param name der Name des Lichts
	 */
	public void setName(String name);
	
	/**
	 * Gibt die Farbe des Lichts zurück.
	 * @return
	 */
	public Color getColor();
	
	/**
	 * Setzt die Farbe des Lichts.
	 * @param color
	 */
	public void setColor(Color color);
	
	/**
	 * Gibt die Intensität zurück.
	 * @return
	 */
	public float getIntensity();
	
	/**
	 * Setzt die Intensität des Lichts.
	 * @param intensity
	 */
	public void setIntensity(float intensity);
	
	/**
	 * Gibt die Position der Lichtquelle zurück.
	 * @return
	 */
	public Vector3d getPosition();
	
	/**
	 * Setzt die Position der Lichtquelle.
	 */
	public void setPosition(Vector3d position);
}
