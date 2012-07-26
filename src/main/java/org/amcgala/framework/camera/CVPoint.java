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

import com.google.common.base.Objects;

/**
 * Diese Klasse repräsentiert einen Punkt im kanonischen View Volume.
 * Einen CVPoint erhält man als Ergebnis der Projektion.
 */
public class CVPoint {

    public double x;
    public double y;
    public double z;

    /**
     * Erzeugt einen neuen CVPunkt.
     *
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    public CVPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("x", x).add("y", y).add("z", z).toString();
    }

}
