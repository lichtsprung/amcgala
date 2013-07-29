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
import org.amcgala.math.Vector3;

/**
 * Eine abstrakte Implementierung des {@code Camera} Interface.
 *
 * @author Robert Giacinto
 */
public abstract class AbstractCamera implements Camera {

    /**
     * "oben" Vektor
     */
    protected Vector3 up;
    /**
     * Position der Kamera
     */
    protected Vector3 location;
    /**
     * Punkt, zu dem die Kamera blickt
     */
    protected Vector3 direction;
    /**
     * lokale x-Achse der Kamera
     */
    protected Vector3 u;
    /**
     * lokale y-Achse der Kamera
     */
    protected Vector3 v;
    /**
     * lokale z-Achse der Kamera
     */
    protected Vector3 n;
    /**
     * Die Projektionsmatrix
     */
    protected Matrix projectionMatrix;

    protected Matrix inverseProjectionMatrix;

    protected boolean parallel;

    protected int width;
    protected int height;

    public AbstractCamera() {
        inverseProjectionMatrix = Matrix.identity(4, 4);
        projectionMatrix = Matrix.identity(4, 4);
    }

    /**
     * Gibt die Projektionsmatrix der Kamera zurück.
     *
     * @return die aktuelle Projektionsmatrix
     */
    protected abstract Matrix getProjectionMatrix();

    /**
     * Gibt die Blickrichtung der Kamera zurück.
     *
     * @return die aktuelle Blickrichtung der Kamera
     */
    @Override
    public Vector3 getDirection() {
        // return quaternion.getRotationColumn(2);
        return direction;
    }

    /**
     * Ändert die Blickrichtung der Kamera.
     *
     * @param direction die neue Blickrichtung
     */
    @Override
    public void setDirection(Vector3 direction) {
        this.direction = direction;
        update();
    }

    /**
     * Gibt die Position der Kamera zurück.
     *
     * @return die Position der Kamera
     */
    @Override
    public Vector3 getPosition() {
        return location;
    }

    /**
     * Ändert die Position der Kamera.
     *
     * @param position die neue Position
     */
    @Override
    public void setPosition(Vector3 position) {
        this.location = position;
        update();
    }

    /**
     * Gibt den Oben-Vektor der Kamera zurück.
     *
     * @return der Oben-Vektor
     */
    @Override
    public Vector3 getVup() {
        return up;
    }

    /**
     * Ändert den Oben-Vektor der Kamera.
     *
     * @param up der neue Oben-Vektor
     */
    @Override
    public void setVup(Vector3 up) {
        this.up = up;
        update();
    }

    public boolean isParallel() {
        return parallel;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }
}
