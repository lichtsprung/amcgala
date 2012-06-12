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
package org.amcgala.example.container;

import org.amcgala.Framework;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.CompositeShape;
import org.amcgala.framework.shape.Line;

/**
 * Zeigt die funktionsweise der Klasse <i>Paint</i> mit Koordinaten.
 *
 * @author Sascha Lemke
 */
public class ContainerMain extends Framework {

    public ContainerMain(int width, int height) {
        super(width, height);
    }

    public static void main(String[] args) {
        Framework fm = new ContainerMain(800, 600);
        fm.start();
    }

    @Override
    public void initGraph() {
        Line[] linien = new Line[4];
        linien[0] = new Line(new Vector3d(-100, 0, 0), new Vector3d(100, 0, 0));
        linien[1] = new Line(new Vector3d(100, 0, 0), new Vector3d(100, 100, -100));
        linien[2] = new Line(new Vector3d(100, 100, -100), new Vector3d(-100, 100, -100));
        linien[3] = new Line(new Vector3d(-100, 100, -100), new Vector3d(-100, 0, 0));

        //this.getCamera().setPosition(new Vector3d(50, 0, -20));

        Line[] linien2 = new Line[4];
        linien2[0] = new Line(new Vector3d(-100, 0, 0), new Vector3d(100, 0, 0));
        linien2[1] = new Line(new Vector3d(100, 0, 0), new Vector3d(100, 100, 0));
        linien2[2] = new Line(new Vector3d(100, 100, 0), new Vector3d(-100, 100, 0));
        linien2[3] = new Line(new Vector3d(-100, 100, 0), new Vector3d(-100, 0, 0));


        CompositeShape viereck = new CompositeShape(linien);
        viereck.add(linien2);
        System.out.println(viereck.toString());
        add(viereck);
    }
}
