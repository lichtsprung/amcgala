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

import org.amcgala.framework.math.Vector3;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Pixel;

/**
 * Das {@code Camera} Interface muss von allen virtuellen Kameras des Frameworks
 * implementiert werden.
 *
 * @author Robert Giacinto
 */
public interface Camera {


    /**
     * Gibt die Blickrichtung der Kamera zurück.
     *
     * @return die aktuelle Blickrichtung der Kamera
     */
    Vector3 getDirection();

    /**
     * Gibt die Koordinaten des Vektors im Bildraum zurück.
     *
     * @param vector3d der zu projezierende Vektor
     * @return die Koordinaten des Vektors im Bildraum
     */
    Pixel getImageSpaceCoordinates(Vector3 vector3d);

    /**
     * Gibt die Position der Kamera zurück.
     *
     * @return die Position der Kamera
     */
    Vector3 getPosition();

    /**
     * Gibt den Oben-Vektor der Kamera zurück.
     *
     * @return der Oben-Vektor
     */
    Vector3 getVup();

    /**
     * Ändert die Blickrichtung der Kamera.
     *
     * @param direction die neue Blickrichtung
     */
    void setDirection(Vector3 direction);

    /**
     * Ändert die Position der Kamera.
     *
     * @param position die neue Position
     */
    void setPosition(Vector3 position);

    /**
     * Ändert den Oben-Vektor der Kamera.
     *
     * @param vup der neue Oben-Vektor
     */
    void setVup(Vector3 vup);

    /**
     * Aktualisiert die Projektionsmatrix des Kameraobjekts. Diese Methode
     * sollte immer dann aufgerufen werden, wenn etwas an der Kamera verändert
     * wurde.
     */
    void update();

    int getWidth();

    int getHeight();

    void setWidth(int width);

    void setHeight(int height);
}
