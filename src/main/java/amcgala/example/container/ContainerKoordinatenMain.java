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
package amcgala.example.container;

import amcgala.Framework;
import amcgala.framework.math.Vector3d;
import amcgala.framework.shape.Container;

/**
 * Zeigt die funktionsweise der Klasse <i>Paint</i> mit Koordinaten.
 * @author Sascha Lemke
 */
public class ContainerKoordinatenMain extends Framework {

	public ContainerKoordinatenMain(int width, int height) {
		super(width, height);
	}
	
	public static void main(String[] args) {
		Framework fm = new ContainerKoordinatenMain(800, 600);
		fm.start();
	}

	@Override
	public void initGraph() {
		
		Container quadrat = new Container();		
		quadrat.add(new Vector3d(-100, -100, 0), new Vector3d(100, -100, 0));
		quadrat.add(new Vector3d(100, -100, 0), new Vector3d(100, 100, 0));
		quadrat.add(new Vector3d(100, 100, 0), new Vector3d(-100, 100, 0));
		quadrat.add(new Vector3d(-100, 100, 0), new Vector3d(-100, -100, 0));

		System.out.println(quadrat.toString());
	
		add(quadrat);
	}

}
