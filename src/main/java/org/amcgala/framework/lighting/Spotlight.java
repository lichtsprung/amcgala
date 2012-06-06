package org.amcgala.framework.lighting;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Color;

/**
 * Klasse für das Spotlight, dient zur Berechnung einer Lichtquelle
 * die in eine bestimmte Richtung innerhalb eines bestimmten Bereiches 
 * Licht abstrahlt das mit Entfernung abnimmt.
 * @author Sascha Lemke
 */
public class Spotlight implements Light {
	
	private String name;
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Color interpolate(Color color, Vector3d pixelposition) {
		// mächtige interpolate methode für das spotlight
		
		return new Color(0, 0 ,0);
	}

}
