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
package org.amcgala.framework.appearance;

/**
 * Die Basisoberflächenklasse für alle Shapeobjekte.
 * Definiert die Oberflächeneigenschaften für ein ganzes Shapeobjekt,
 * würde normalerweise nur für ein Pixel gelten.
 * @author Sascha Lemke
 */
public class BasicAppearance implements Appearance {

	private double reflexionsKoeffizient = 1;
	private double spiegelReflexionsKoeffizient = 0.5;
	private double spiegelReflexionsExponent = 2;
	
	@Override
	public double getReflection() {
		return this.reflexionsKoeffizient;
	}

	@Override
	public double getSpiegelkoeffizient() {
		return this.spiegelReflexionsKoeffizient;
	}

	@Override
	public double getSpiegelReflectionExponent() {
		return this.spiegelReflexionsExponent;
	}

}
