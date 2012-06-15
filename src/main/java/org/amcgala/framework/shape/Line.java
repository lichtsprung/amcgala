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
package org.amcgala.framework.shape;

import com.google.common.base.Objects;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eine Linie im 3d Raum.
 *
 * @author Robert Giacinto
 */
public class Line extends Shape {

    public double x1, y1, z1;
    public double x2, y2, z2;
    private Vector3d start, end;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        if (x1 > x2) { // vertausche Punkte
            this.x1 = x2;
            this.y1 = y2;
            this.x2 = x1;
            this.y2 = y1;
        }

        if (x1 == x2 && y1 > y2) { // Vertikale von y1 unten nach y2 oben
            this.y1 = y2;
            this.y2 = y1;
        }

        start = new Vector3d(x1, y1, -1);
        end = new Vector3d(x2, y2, -1);
    }

    public Line(Vector3d start, Vector3d end) {
        this.x1 = start.x;
        this.y1 = start.y;
        this.z1 = start.z;
        this.x2 = end.x;
        this.y2 = end.y;
        this.z2 = end.z;
    }

    @Override
    public void render(Renderer renderer) {
        // Einbeziehen der Transformationsgruppen. Um Animationen zu beruecksichtigen, die auf die einzelnen Felder zugegriffen
        // haben, werden die start und end Vektoren aktualisiert, bevor sie mit der Transformationsmatrix multipliziert werden.
        start = new Vector3d(x1, y1, z1);
        end = new Vector3d(x2, y2, z2);
        renderer.drawLine(start, end);
    }

    /**
     * toString method.
     */
    public String toString() {

        return Objects.toStringHelper(getClass()).add("x1", x1).add("y1", y1).add("x2", x2).add("y2", y2).toString();
    }

    private static final Logger log = LoggerFactory.getLogger(Line.class.getName());
}
