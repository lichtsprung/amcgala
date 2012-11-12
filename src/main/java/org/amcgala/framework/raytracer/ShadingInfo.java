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

import org.amcgala.Scene;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.tracer.Tracer;

/**
 * Die für das Raytracing benötigten Ergebnisse der Schnittpunktsberechnung.
 */
public class ShadingInfo {
    public double t = Double.POSITIVE_INFINITY;
    public String label = "none";
    public Vector3d hitPoint;
    public Vector3d normal;
    public RGBColor color;
    public Ray ray;
    public Tracer tracer;
    public int depth;
    public Scene scene;


    @Override
    public String toString() {
        return "ShadingInfo{" +
                "t=" + t +
                ", label='" + label + '\'' +
                ", hitPoint=" + hitPoint +
                ", normal=" + normal +
                ", color=" + color +
                ", ray=" + ray +
                ", tracer=" + tracer +
                ", depth=" + depth +
                ", scene=" + scene +
                '}';
    }
}
