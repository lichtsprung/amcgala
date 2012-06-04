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
package amcgala.example.lighting;

import java.awt.event.KeyEvent;


import amcgala.Framework;
import amcgala.framework.animation.interpolation.LinearInterpolation;
import amcgala.framework.event.InputHandler;
import amcgala.framework.math.Vector3d;
import amcgala.framework.scenegraph.Node;
import amcgala.framework.scenegraph.transform.RotationY;

import amcgala.framework.lighting.AmbientLight;
import amcgala.framework.renderer.Color;
import amcgala.framework.shape.shape3d.Mesh;

import com.google.common.eventbus.Subscribe;

/**
 * Beispielklasse für Beleuchtung in amCGAla. Zeigt ein Objekt das von einer Lichtquelle beleuchtet wird.
 * @author Sascha Lemke
 */
public class LightingDemo01 extends Framework implements InputHandler {

	private AmbientLight ambient;
	
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
		fm.setBackgroundColor(new java.awt.Color(212, 212, 212)); // setzt die Hintergrundfarbe für den Frame
	}

	/**
	 * Erstellt die Szene.
	 */
	@Override
	public void initGraph() {
		this.registerInputEventHandler(this);
		
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
        this.ambient = new AmbientLight("TestAmbientLight", 0.1, new Color(255, 255, 255));
        n.addLight(ambient);
	}

	@Subscribe
	public void changeIntensity(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			this.ambient.setIntensity(this.ambient.getIntensity() + 0.05);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.ambient.setIntensity(this.ambient.getIntensity() - 0.05);
		}
	}
}
