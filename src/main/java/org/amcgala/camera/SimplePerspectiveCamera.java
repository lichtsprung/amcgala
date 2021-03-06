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
package org.amcgala.camera;

import org.amcgala.math.Matrix;
import org.amcgala.math.Quaternion;
import org.amcgala.math.Vector3d;
import org.amcgala.renderer.Pixel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eine naive Implementierung einer perspektivischen Kamera.
 *
 * @author Robert Giacinto
 */
public final class SimplePerspectiveCamera extends AbstractCamera {

    private double d;
    private Quaternion quaternion;

    /**
     * Erzeugt eine neue Kamera an einer Position mit einem bestimmten
     * Blickpunkt.
     *
     * @param vup       Das Oben der Kamera
     * @param location  Die Position der Kamera
     * @param direction Der Punkt, zu dem die Kamera blickt
     * @param d         der Abstand der Kamera zur Projektionsebene. Umso kleiner der
     *                  Wert desto größer die perspektivische Wirkung
     */
    public SimplePerspectiveCamera(Vector3d vup, Vector3d location, Vector3d direction, double d) {
        this.up = vup;
        this.location = location;
        this.direction = direction;
        this.d = d;
        quaternion = new Quaternion(direction, 0);
        update();
    }

    @Override
    public Matrix getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public void update() {
        this.n = direction.sub(location).times(-1);
        this.u = up.cross(n).normalize();
        this.v = n.cross(u).normalize();

        double[][] vdValues = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1.0 / d, 1}
        };

        Matrix vd = new Matrix(vdValues);

        Vector3d d = Vector3d.createVector3d(location.dot(u), location.dot(v), location.dot(n));

        double[][] viewValues = {
                {u.getX(), u.getY(), u.getZ(), d.x},
                {v.getX(), v.getY(), v.getZ(), d.y},
                {n.getX(), n.getY(), n.getZ(), d.z},
                {0, 0, 0, 1}
        };
        Matrix kt = new Matrix(viewValues);
        projectionMatrix = vd.times(kt);
    }

    /**
     * Gibt den Abstand der Kamera zur Projektionsebene zurück.
     *
     * @return der aktuelle Abstand
     */
    public double getD() {
        return d;
    }

    /**
     * Ändert den Abstand der Kamera zur Projektionsebene.
     *
     * @param d der neue Abstand
     */
    public void setD(double d) {
        this.d = d;
        update();
    }


    @Override
    public Pixel project(Vector3d vector3d) {
        Matrix point = projectionMatrix.times(vector3d.toMatrix());
        Pixel p = new Pixel(point.get(0, 0) / point.get(3, 0), point.get(1, 0) / point.get(3, 0));
        return p;
    }

    private static final Logger log = LoggerFactory.getLogger(OrthographicCamera.class);
}
