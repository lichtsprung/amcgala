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

import org.amcgala.Framework;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.lighting.AmbientLight;
import org.amcgala.framework.lighting.PointLight;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.shape3d.Mesh;

import com.google.common.eventbus.Subscribe;

/**
 * Beispiel für die Verwendung des Pointlights.
 * @author Sascha Lemke
 */
public class PointDemo extends Framework implements InputHandler {

	private PointLight l2;
	
	/**
	 * Konstruktormethode.
	 * @param width Die Breite des Frames
	 * @param height Die Höhe des Frames
	 */
	public PointDemo(int width, int height) {
		super(width, height);
	}

	/**
	 * Mainmethode.
	 * @param args
	 */
	public static void main(String[] args) {
		Framework fm = new PointDemo(800, 600);
		fm.start();
	}

	/**
	 * Erstellt die Szene.
	 */
	@Override
	public void initGraph() {
		this.registerInputEventHandler(this);
		Vector3d lp = new Vector3d(150, 100, 1);
		
		Node n = new Node("light");
        
        Mesh m = new Mesh(new Vector3d(-400, -300, 0), 800, 600, 100);
        m.color = new Color(0, 0, 0);
        n.addShape(m);
        add(n);
        
        AmbientLight ambient = new AmbientLight("TestAmbientLight", 0.5, new Color(255, 255, 255));
        l2 = new PointLight("Pointlight", ambient, lp);
        n.addLight(l2);
	}

	@Subscribe
	public void setIntensity(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			double i = this.l2.getIntensity() + 0.025;
			if( i > 1 ) i = 1;
			this.l2.setIntensity(i);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			double i = this.l2.getIntensity() - 0.05;
			if( i < 0 ) i = 0;
			this.l2.setIntensity(i);
		}
	}
}
