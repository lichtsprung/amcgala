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
package org.amcgala.framework.raytracer.tracer;


import org.amcgala.Scene;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.Ray;
import org.amcgala.framework.raytracer.ShadingInfo;

/**
 * Ein Raytracer stellt eine Szene über die Schnittpunktsberechnungen der vorhandenen Objekte mit "Sehstrahlen" dar.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class SimpleTracer implements Tracer {


    @Override
    public RGBColor trace(Ray ray, Scene scene) {
        ShadingInfo result = new ShadingInfo();

        /*
         * TODO
         * Für jedes Shape
         *      Berechne den Schnittpunkt, der am nächsten zum Beobachter liegt
         *      Wenn es einen Schnittpunkt gibt
         *          Speichere den neuen Wert in der Variable result
         *
         * Gibt es einen sichtbaren Schnittpunkt
         *      Gib die Farbe an des geschnittenen Objekts zurück
         * Sonst
         *      Gib die Hintergrundfarbe der Szene zurück
         */

        return null;
    }

}
