package amcgala.framework.shape;

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
import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;

/**
 * Polygonobjekt fuer die Koerperdarstellung im 3D Raum
 *
 * @author Steffen Troester
 *
 */
public class Polygon extends Shape {

    private BresenhamLine3d bl1;
    private BresenhamLine3d bl2;
    private BresenhamLine3d bl3;
    private BresenhamLine3d bl4;

    public void move(double x, double y, double z) {
        if (bl1 != null && bl2 != null && bl3 != null) {
            bl1.x1 += x;
            bl1.x2 += x;
            bl1.y1 += y;
            bl1.y2 += y;
            bl1.z1 += z;
            bl1.z2 += z;

            bl2.x1 += x;
            bl2.x2 += x;
            bl2.y1 += y;
            bl2.y2 += y;
            bl2.z1 += z;
            bl2.z2 += z;

            bl3.x1 += x;
            bl3.x2 += x;
            bl3.y1 += y;
            bl3.y2 += y;
            bl3.z1 += z;
            bl3.z2 += z;
            if (bl4 != null) {
                bl4.x1 += x;
                bl4.x2 += x;
                bl4.y1 += y;
                bl4.y2 += y;
                bl4.z1 += z;
                bl4.z2 += z;
            }
        }
    }

    /*
     * Verhindern des Standardkonstruktors.
     */
    private Polygon() {
    }

    public Polygon(Vector3d v1, Vector3d v2, Vector3d v3) {
        bl1 = new BresenhamLine3d(v1, v2);
        bl2 = new BresenhamLine3d(v2, v3);
        bl3 = new BresenhamLine3d(v3, v1);
    }

    public Polygon(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4) {
        bl1 = new BresenhamLine3d(v1, v2);
        bl2 = new BresenhamLine3d(v2, v3);
        bl3 = new BresenhamLine3d(v3, v4);
        bl4 = new BresenhamLine3d(v4, v1);
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        if (bl1 != null && bl2 != null && bl3 != null) {
            bl1.render(transformation, camera, renderer);
            bl2.render(transformation, camera, renderer);
            bl3.render(transformation, camera, renderer);
            if (bl4 != null) {
                bl4.render(transformation, camera, renderer);
            }
        }

    }

    @Override
    public String toString() {
        return "Polygon{" + "line 1=" + bl1 + ", line 2=" + bl2 + ", line 3="
               + bl3 + '}';
    }
}
