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
package org.amcgala.framework.raytracer;

import org.amcgala.framework.math.Vector3d;

/**
 * Ein Strahl, der vom {@link Raytracer} in die {@link org.amcgala.Scene} geschickt wird. Er ist über die Vektorform d
 * definiert als r(t) = origin + t * direction.
 */
public class Ray {
    public Vector3d origin;
    public Vector3d direction;

    /**
     * Erstellt eine neue Gerade, die über einen Ursprungs- und Richtungsvektor definiert ist.
     * @param origin der Ursprungsvektor
     * @param direction der Richtungsvektor
     */
    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }
}
