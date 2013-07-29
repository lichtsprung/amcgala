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
import org.amcgala.math.Vector3;
import org.amcgala.renderer.Pixel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Diese Klasse implementiert eine Kamera mit perspektivischer Projektion. Aus
 * den Vektoren für Position, Blickrichtung und "oben" wird die Projektionsmatrix
 * bestimmt.
 *
 * @author Robert Giacinto
 */
public final class PerspectiveCamera extends AbstractCamera {

    private static final Logger log = LoggerFactory.getLogger(PerspectiveCamera.class);

    private double fieldOfView;
    private double aspect;
    private double near;
    private double far;

    /**
     * Projektionsmatrix für die Transformation in das Kamerakoordinatensystem
     */
    private Matrix view;

    /**
     * Ausrichtung der Kamera
     */
    private Quaternion rotation;

    private PerspectiveCamera(double fieldOfView, double aspect, double near, double far) {
        this.fieldOfView = fieldOfView;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        update();
    }


    public void setLens(double focalLength, double height) {
        fieldOfView = 2 * Math.atan(height / (focalLength * 2)) * (180 / Math.PI);
        update();
    }

    @Override
    public Matrix getProjectionMatrix() {
        return projectionMatrix;
    }


    @Override
    public void update() {
        aspect = width / height;
        projectionMatrix = Matrix.getPerspective(fieldOfView, aspect, near, far);
    }

    @Override
    public Pixel getImageSpaceCoordinates(Vector3 vector3d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
