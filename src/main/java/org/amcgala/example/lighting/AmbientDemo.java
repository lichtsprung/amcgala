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

import java.awt.event.KeyEvent;

import org.amcgala.framework.lighting.AmbientLight;
import org.amcgala.framework.shape.shape3d.Mesh;

import org.amcgala.Framework;
import org.amcgala.framework.animation.interpolation.LinearInterpolation;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.transform.RotationY;

import com.google.common.eventbus.Subscribe;

/**
 * Beispiel für die Verwendung des ambienten Lichts.
 * @author Sascha Lemke
 */
public class AmbientDemo extends Framework implements InputHandler {

	private AmbientLight ambient;
	/**
	 * Konstruktormethode.
	 * @param width Die Breite des Frames
	 * @param height Die Höhe des Frames
	 */
	public AmbientDemo(int width, int height) {
		super(width, height);
	}

	/**
	 * Mainmethode.
	 * @param args
	 */
	public static void main(String[] args) {
		Framework fm = new AmbientDemo(800, 600);
		fm.start();
		fm.setBackgroundColor(new java.awt.Color(212, 212, 212));
	}

	/**
	 * Erstellt die Szene.
	 */
	@Override
	public void initGraph() {
		this.registerInputEventHandler(this);
		
		Node n = new Node("light");
        RotationY rotY = new RotationY();
        rotY.setInterpolationPhi(new LinearInterpolation(0, 4 * Math.PI, 250, true));
        n.setTransformation(rotY);
        
        Mesh m = new Mesh(new Vector3d(-100, -100, 0), 200, 200, 50);
        m.color = new Color(0, 0, 255);
        n.addShape(m);
        add(n);
        
        this.ambient = new AmbientLight("TestAmbientLight", 1, new Color(255, 255, 255));
        n.addLight(ambient);
	}

	@Subscribe
	public void changeIntensity(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			double i = this.ambient.getIntensity() + 0.025;
			if( i > 1 ) i = 1;
			this.ambient.setIntensity(i);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			double i = this.ambient.getIntensity() - 0.05;
			if( i < 0 ) i = 0;
			this.ambient.setIntensity(i);
		}
	}
}

