package org.amcgala.framework.shape;

/*
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.common.base.Objects;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Polygonobjekt fuer die Koerperdarstellung im 3D Raum
 *
 * @author Steffen Troester
 */
public class Polygon extends Shape {

    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private Vector3d norm;

    public void move(double x, double y, double z) {
        if (line1 != null && line2 != null && line3 != null) {
            line1.x1 += x;
            line1.x2 += x;
            line1.y1 += y;
            line1.y2 += y;
            line1.z1 += z;
            line1.z2 += z;

            line2.x1 += x;
            line2.x2 += x;
            line2.y1 += y;
            line2.y2 += y;
            line2.z1 += z;
            line2.z2 += z;

            line3.x1 += x;
            line3.x2 += x;
            line3.y1 += y;
            line3.y2 += y;
            line3.z1 += z;
            line3.z2 += z;

            if (line4 != null) {
                line4.x1 += x;
                line4.x2 += x;
                line4.y1 += y;
                line4.y2 += y;
                line4.z1 += z;
                line4.z2 += z;
            }
        }
    }

    /*
      * Verhindern des Standardkonstruktors.
      */
    private Polygon() {
    }

    public Polygon(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d norm) {
        checkNotNull(v1);
        checkNotNull(v2);
        checkNotNull(v3);
        checkNotNull(norm);
        line1 = new Line(v1, v2);
        line2 = new Line(v2, v3);
        line3 = new Line(v3, v1);
        this.norm = norm;
    }

    public Polygon(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4,
                   Vector3d norm) {
        checkNotNull(v1);
        checkNotNull(v2);
        checkNotNull(v3);
        checkNotNull(v4);
        checkNotNull(norm);
        line1 = new Line(v1, v2);
        line2 = new Line(v2, v3);
        line3 = new Line(v3, v4);
        line4 = new Line(v4, v1);
        this.norm = norm;
    }

    @Override
    public void render(Renderer renderer) {

        // TODO das muss irgendwie ausgelagert werden und sollte nicht Teil des Shapes sein.
        if (renderer.getCamera().getDirection().dot(this.norm) < 0) {
            return;
        }

        // rendering
        if (line1 != null && line2 != null && line3 != null) {
            line1.render(renderer);
            line2.render(renderer);
            line3.render(renderer);
            if (line4 != null) {
                line4.render(renderer);
            }
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("line1", line1).add("line2", line2).add("line3", line3).add("line4", line4).toString();
    }

    private static final Logger log = LoggerFactory.getLogger(Polygon.class.getName());
}
