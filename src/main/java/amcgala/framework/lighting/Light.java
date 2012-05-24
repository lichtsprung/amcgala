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
 * Klasse für die Darstellung von Lichtquellen innerhalb einer Szene.<br />
 * Es gibt drei verschiedene Art von Lichtquellen die zur Verfügung gestellt werden:<br /><br />
 * - Ambientlight<br />
 * - Pointlight <br />
 * - Spotlight <br />
 * @author Sascha Lemke
 */
public class Light  {
	
	private String name;
	private Vector3d position;
	private double intensity = 0.5;
	private double reflexionskoeffizient = 0.5;
	private String mode;
	private Color color = new Color(255, 255, 255);
	
	/**
	 * Konstruktor für das Ambientlight.
	 * @param name Der Name der Lichtquelle
	 * @param intensity Die Intensität der Lichtquelle
	 * @param reflexion Die Reflexionsstärke des Objektes
	 */
	public Light(String name, double intensity, double reflexion) {
		this.name = name;
		this.mode = "ambientlight";
		if(intensity > 0.0 && intensity < 1.0 && reflexion > 0.0 && reflexion < 1.0) {
			this.intensity = intensity;
			this.reflexionskoeffizient = reflexion;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Konstruktor für das Pointlight.
	 * @param name Der Name der Lichtquelle
	 * @param intensity Die Intensität der Lichtquelle
	 * @param reflexion Die Reflexionsstärke des Objektes
	 * @param position Die Position der Lichtquelle
	 */
	public Light(String name, double intensity, double reflexion, Vector3d position) {
		this.name = name;
		this.mode = "pointlight";
		if(intensity > 0.0 && intensity < 1.0 && reflexion > 0.0 && reflexion < 1.0) {
			this.intensity = intensity;
			this.reflexionskoeffizient = reflexion;
		} else {
			throw new IllegalArgumentException();
		}
		this.position = position;
	}
	
	/**
	 * Gibt den Namen der Lichtquelle zurück.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setzt den Namen der Lichtquelle.
	 * @param name Der Name der Lichtquelle
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gibt den Modus der Lichtquelle zurück.
	 */
	public String getMode() {
		return this.mode;
	}

	/**
	 * Gibt die Intensität der Lichtquelle zurück.
	 */
	public double getIntensity() {
		return this.intensity;
	}

	/**
	 * Setzt die Intensität der Lichtquelle.
	 * @param intensity Die Intensität der Lichtquelle
	 */
	public void setIntensity(double intensity) {
		if(intensity > 0 && intensity < 1) {
			this.intensity = intensity;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Gibt die Position des Lichts im 3D-Raum zurück.
	 */
	public Vector3d getPosition() {
		return this.position;
	}

	/**
	 * Setzt die Position des Lichts im 3D-Raum.
	 * @param position Die Position der Lichtquelle
	 */
	public void setPosition(Vector3d position) {
		this.position = position;
	}   
	
	/**
	 * Gibt die Farbe der Lichtquelle zurück.
	 * @return Die Farbe der Lichtquelle
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Setzt die Lichtquelle auf die angegebene Farbe.
	 * @param color Die Farbe
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Gibt den Reflexionskoeffizienten zurück.
	 * @return Der Reflexionskoeffizient
	 */
	public double getReflexionskoeffizent() {
		return this.reflexionskoeffizient;
	}
	
	/**
	 * Setzt den Reflexionskoeffizienten.
	 * @param reflexionskoeffizient Der neue Reflexionskoeffizient (0 > reflexionskoeffizient < 1)
	 */
	public void setReflexionskoeffizent(double reflexionskoeffizient) {
		if(reflexionskoeffizient > 0 && reflexionskoeffizient < 1) {
			this.reflexionskoeffizient = reflexionskoeffizient;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	
	/**
     * Berechnet die Farbe eines Pixels.
     * @return die Farbe des Lichts
     */
	public Color interpolate(Color color) {
		int r, g, b;
		if(mode == "ambientlight") {
			r = (int) (this.intensity * color.getR() * this.reflexionskoeffizient);
			b = (int) (this.intensity * color.getB() * this.reflexionskoeffizient);
			g = (int) (this.intensity * color.getG() * this.reflexionskoeffizient);
			
		} else if(mode == "pointlight") {
			r = color.getR();
			b = color.getB();
			g = color.getG();
		} else { 
			r = color.getR();
			b = color.getB();
			g = color.getG();
			
		}
		//System.out.println(b);
		return new Color(r, g, b);
	}
	
	/**
	 * Gibt die Parameter der Lichtquelle zurück.
	 */
	@Override
	public String toString() {
		String output = "";
		if(mode == "ambientlight") {
			output = "Light: { name: " + this.name + ", intensity: " + this.intensity + ", Reflexionskoeffizient: " + this.reflexionskoeffizient + " }";
		} else if(mode == "pointlight") {
			output = "pointlight";
		} else {
			// spotlight
			output = "spotlight";
		}
		return output;
	}
}
