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

import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.shape.shape3d.Mesh;

import org.amcgala.Framework;
import org.amcgala.framework.animation.interpolation.LinearInterpolation;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.transform.RotationY;

/**
 * Beispielklasse für Beleuchtung in amCGAla. Zeigt ein Objekt das von einer Lichtquelle beleuchtet wird.
 * @author Sascha Lemke
 */
public class LightingDemo01 extends Framework {

	/**
	 * Konstruktormethode.
	 * @param width Die Breite des Frames
	 * @param height Die Höhe des Frames
	 */
	public LightingDemo01(int width, int height) {
		super(width, height);
	}

	/**
	 * Mainmethode.
	 * @param args
	 */
	public static void main(String[] args) {
		Framework fm = new LightingDemo01(800, 600);
		fm.start();
		fm.setBackgroundColor(new java.awt.Color(161, 217, 240)); // setzt die Hintergrundfarbe für den Frame
	}

	/**
	 * Erstellt die Szene.
	 */
	@Override
	public void initGraph() {
		
		Node n = new Node("rotating box");
        RotationY rotY = new RotationY();
        rotY.setInterpolationPhi(new LinearInterpolation(0, 4 * Math.PI, 250, true));
        n.setTransformation(rotY);
        
        // mesh
        Mesh m = new Mesh(new Vector3d(0,0,0), 100, 50, 50);
        m.color = new Color(255, 0, 0);
        n.addShape(m);
        add(n);
        
        // licht
        Light l = new Light("light1", 0.5, 0.99);
        System.out.println(l.toString());
        n.addLight(l);
	}

}
