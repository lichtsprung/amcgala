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
import amcgala.framework.shape.BresenhamLine3d;
import amcgala.framework.shape.Container;

/**
 * Zeigt die funktionsweise der Klasse <i>Paint</i> mit Koordinaten.
 *
 * @author Sascha Lemke
 */
public class ContainerBresenhamLine3dMain extends Framework {

    public ContainerBresenhamLine3dMain(int width, int height) {
        super(width, height);
    }

    public static void main(String[] args) {
        Framework fm = new ContainerBresenhamLine3dMain(800, 600);
        fm.start();
    }

    @Override
    public void initGraph() {
        BresenhamLine3d[] linien = new BresenhamLine3d[4];
        linien[0] = new BresenhamLine3d(new Vector3d(-100, 0, 0), new Vector3d(100, 0, 0));
        linien[1] = new BresenhamLine3d(new Vector3d(100, 0, 0), new Vector3d(100, 100, -100));
        linien[2] = new BresenhamLine3d(new Vector3d(100, 100, -100), new Vector3d(-100, 100, -100));
        linien[3] = new BresenhamLine3d(new Vector3d(-100, 100, -100), new Vector3d(-100, 0, 0));

        this.getCamera().setPosition(new Vector3d(50, 0, -20));

        BresenhamLine3d[] linien2 = new BresenhamLine3d[4];
        linien2[0] = new BresenhamLine3d(new Vector3d(-100, 0, 0), new Vector3d(100, 0, 0));
        linien2[1] = new BresenhamLine3d(new Vector3d(100, 0, 0), new Vector3d(100, 100, 0));
        linien2[2] = new BresenhamLine3d(new Vector3d(100, 100, 0), new Vector3d(-100, 100, 0));
        linien2[3] = new BresenhamLine3d(new Vector3d(-100, 100, 0), new Vector3d(-100, 0, 0));


        Container viereck = new Container(linien);
        viereck.add(linien2);
        System.out.println(viereck.toString());
        add(viereck);
    }
}
