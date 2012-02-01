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
package amcgala.example.paint;

import amcgala.Framework;
import amcgala.framework.math.Vector3d;
import amcgala.framework.shape.BresenhamLine3d;
import amcgala.framework.shape.Paint;

/**
 * Zeigt die funktionsweise der Klasse <i>Paint</i> mit Koordinaten.
 * @author Sascha Lemke
 */
public class PaintKoordinatenMain extends Framework {

	public PaintKoordinatenMain(int width, int height) {
		super(width, height);
	}
	
	public static void main(String[] args) {
		Framework fm = new PaintKoordinatenMain(800, 600);
		fm.start();
	}

	@Override
	public void initGraph() {
		Vector3d[] koordinaten = new Vector3d[6];
		koordinaten[0] = new Vector3d(-100, 0, 0);
		koordinaten[1] = new Vector3d(100, 0, 0);
		koordinaten[2] = new Vector3d(0, 100, 0);
		koordinaten[3] = new Vector3d(-100, 0, 0);
		koordinaten[4] = new Vector3d(0, -100, 0);
		koordinaten[5] = new Vector3d(100, 0, 0);
		
		BresenhamLine3d[] linien = new BresenhamLine3d[4];
		linien[0] = new BresenhamLine3d(new Vector3d(-100, -100, 0), new Vector3d(100, -100, 0));
		linien[1] = new BresenhamLine3d(new Vector3d(100, -100, 0), new Vector3d(100, 100, 0));
		linien[2] = new BresenhamLine3d(new Vector3d(100, 100, 0), new Vector3d(-100, 100, 0));
		linien[3] = new BresenhamLine3d(new Vector3d(-100, 100, 0), new Vector3d(-100, -100, 0));
		
		Paint quadrat = new Paint(koordinaten);
		System.out.println(quadrat.toString());
		quadrat.add(linien);
		
		add(quadrat);
	}

}
