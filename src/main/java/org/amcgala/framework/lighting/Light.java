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

public interface Light {
	
	/**
	 * Gibt den Namen der Lichtquelle zurück.
	 * @return Der Name der Lichtquelle
	 */
	public abstract String getName();

	/**
	 * Setzt den Namen der Lichtquelle.
	 * @param name Der neue Name der Lichtquelle
	 */
	public abstract void setName(String name);
	
	/**
	 * Berechnet die Farbe eines Pixels.
	 * @param color Die aktuelle Farbe des Pixels
	 * @param pixelposition Die aktuelle Position des Pixels
	 * @return die neue Farbe des Pixels auf Basis des Lichts
	 */
	public abstract Color interpolate(Color color, Vector3d pixelposition);

}