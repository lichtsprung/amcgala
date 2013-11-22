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
package org.amcgala.scenegraph.transform;

import org.amcgala.animation.interpolation.Interpolation;
import org.amcgala.math.Matrix;

/**
 * Skalierung um den Faktor s = (sx, sy, sz)
 */
public class Scale implements Transformation {

    private double scaleX, scaleY, scaleZ;
    private Matrix transformMatrix;
    private Interpolation interpolationX;
    private Interpolation interpolationY;
    private Interpolation interpolationZ;

    public Scale(double scaleX, double scaleY, double scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    private void updateMatrix() {
        double[][] values = {
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        };

        transformMatrix = new Matrix(values);
    }

    public Interpolation getInterpolationX() {
        return interpolationX;
    }

    public void setInterpolationX(Interpolation interpolationX) {
        this.interpolationX = interpolationX;
    }

    public Interpolation getInterpolationY() {
        return interpolationY;
    }

    public void setInterpolationY(Interpolation interpolationY) {
        this.interpolationY = interpolationY;
    }

    public Interpolation getInterpolationZ() {
        return interpolationZ;
    }

    public void setInterpolationZ(Interpolation interpolationZ) {
        this.interpolationZ = interpolationZ;
    }

    @Override
    public Matrix getTransformMatrix() {
        return transformMatrix;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
        updateMatrix();
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
        updateMatrix();
    }

    public void setScaleZ(double scaleZ) {
        this.scaleZ = scaleZ;
        updateMatrix();
    }

    public void changeScaleX(double diffX) {
        scaleX += diffX;
        updateMatrix();
    }

    public void changeScaleY(double diffY) {
        scaleY = diffY;
        updateMatrix();
    }

    public void changeScaleZ(double diffZ) {
        scaleZ += diffZ;
        updateMatrix();
    }

    @Override
    public void update() {
        if (interpolationX != null) {
            scaleX = interpolationX.nextValue();
        }
        if (interpolationY != null) {
            scaleY = interpolationY.nextValue();
        }
        if (interpolationZ != null) {
            scaleZ = interpolationZ.nextValue();
        }
        updateMatrix();
    }
}
