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
package org.amcgala.framework.camera;

import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Plane;
import org.amcgala.framework.math.Quaternion;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Pixel;

import java.util.logging.Logger;

/**
 * Diese Klasse implementiert eine Kamera mit perspektivischer Projektion. Aus
 * den Vektoren für Position, Blickrichtung und "oben" wird die Projektionsmatrix
 * bestimmt.
 *
 * @author Robert Giacinto
 */
public final class PerspectiveCamera extends AbstractCamera {

    private static final Logger logger = Logger.getLogger(PerspectiveCamera.class.getName());
    private static final int PLANE_LEFT = 0;
    private static final int PLANE_RIGHT = 1;
    private static final int PLANE_BOTTOM = 2;
    private static final int PLANE_TOP = 3;
    private static final int PLANE_FAR = 4;
    private static final int PLANE_NEAR = 5;
    private static final int FRUSTUM_PLANES_COUNT = 6;
    private static final int MAX_WORLD_PLANES = 6;

    /**
     * Abstand zur near plane
     */
    private double d;
    /**
     * Bildverhältnis
     */
    private double aspectRatio;
    /**
     * Der Sichtwinkel der Kamera
     */
    private double fieldOfView;
    private double viewportLeft;
    private double viewportRight;
    private double viewportTop;
    private double viewportBottom;
    /**
     * Abstand der Kamera zur linken Frustum-Ebene
     */
    private double frustumLeft;
    /**
     * Abstand der Kamera zur rechten Frustum-Ebene
     */
    private double frustumRight;
    /**
     * Abstand der Kamera zur nahen Frustum-Ebene
     */
    private double frustumNear;
    /**
     * Abstand der Kamera zur entfernten Frustum-Ebene
     */
    private double frustumFar;
    /**
     * Abstand der Kamera zur oberen Frustum-Ebene
     */
    private double frustumTop;
    /**
     * Abstand der Kamera zur unteren Frustum-Ebene
     */
    private double frustumBottom;
    /**
     * Breite der Bildschirmausgabe
     */
    private int width;
    /**
     * Höhe der Bildschirmausgabe
     */
    private int height;
    /**
     * Projektionsmatrix für die Transformation in das Kamerakoordinatensystem
     */
    private Matrix view;
    private Plane[] cullingPlanes;
    /**
     * Ausrichtung der Kamera
     */
    private Quaternion rotation;

    private PerspectiveCamera() {
        cullingPlanes = new Plane[FRUSTUM_PLANES_COUNT];
        for (int i = 0; i < cullingPlanes.length; i++) {
            cullingPlanes[i] = new Plane();
        }

        location = Vector3d.createVector3d(0, 0, 0);

        frustumNear = 1.0;
        frustumFar = 2.0;
        frustumLeft = -0.5;
        frustumRight = 0.5;
        frustumTop = 0.5;
        frustumBottom = -0.5;

        viewportLeft = 0.0;
        viewportRight = 1.0;
        viewportTop = 1.0;
        viewportBottom = 0.0;

        fieldOfView = Math.toRadians(75);

        up = Vector3d.UNIT_Y;
    }

    /**
     * Erzeugt eine neue perspektivische Kamera mit einer Bildschirmausgabe in
     * der Auflösung width x height.
     *
     * @param width  die Breite der Ausgabe
     * @param height die Höhe der Ausgabe
     */
    public PerspectiveCamera(int width, int height) {
        this();
        this.width = width;
        this.height = height;

        aspectRatio = width / height;
        update();
    }

    /**
     * Erzeugt eine neue Kamera an einer Position mit einem bestimmten Blickpunkt.
     *
     * @param up        Das Oben der Kamera
     * @param location  Die Position der Kamera
     * @param direction Der Punkt, zu dem die Kamera blickt
     * @param fov       Der Öffnungswinkel der Kamera
     * @param width     die Breite der Bildschirmausgabe
     * @param height    die Höhe der Bildschirmausgabe
     */
    public PerspectiveCamera(Vector3d location, Vector3d direction, Vector3d up, double fov, int width, int height) {
        this();
        this.location = location;
        this.direction = direction;
        this.up = up;
        this.fieldOfView = fov;
        this.aspectRatio = width / height;
        this.width = width;
        this.height = height;
        update();
    }

    @Override
    public Matrix getProjection() {
        return projection;
    }

    @Override
    public CVPoint getClippingSpaceCoordinates(Vector3d vector3d) {
        Matrix point = view.times(vector3d.toMatrix());
        return new CVPoint(point.get(0, 0) / point.get(3, 0), point.get(1, 0) / point.get(3, 0));
    }

    @Override
    public void update() {
        /*
         * Erste Transformation: Weltkoordinaten -> Kamerakoordinaten.
         */
        u = up.cross(n).normalize();
        view = Matrix.getView(location, direction, up, u);


        /*
        * Zweite Transformation: Kamerakoordinaten -> Bildkoordinaten
        */

        projection = Matrix.identity(4, 4);
    }

    @Override
    public Pixel getImageSpaceCoordinates(Vector3d vector3d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
