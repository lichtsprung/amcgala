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
package org.amcgala.example.lighting;

import org.amcgala.Framework;
import org.amcgala.framework.lighting.AmbientLight;
import org.amcgala.framework.lighting.SpotLight;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.shape.shape3d.Mesh;

/**
 * Beispiel für die Verwendung des Spotlights.
 * @author Sascha Lemke
 */
public class SpotDemo extends Framework {

	public SpotDemo(int width, int height) {
		super(width, height);
	}
	
	public static void main(String args[]) {
		SpotDemo sd = new SpotDemo(800, 600);
		sd.start();
	}
	
	@Override
	public void initGraph() {
		AmbientLight ambient = new AmbientLight("TestAmbientLight", 0.4, new Color(255, 255, 255));
		SpotLight l1 = new SpotLight("Spotlight", ambient, 1.0, new Vector3d(0, 0, 100), new Vector3d(0, 0, 1));
		add(l1);
		
        Mesh m = new Mesh(new Vector3d(-400, -300, 0), 800, 600, 100);
        m.color = new Color(255, 0, 0);
        add(m);
        System.out.println(l1.toString());
	}
}
