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

import org.amcgala.Framework;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.sampler.RandomSampler;
import org.amcgala.framework.raytracer.tracer.RecursiveTracer;
import org.amcgala.framework.raytracer.tracer.Tracer;
import org.amcgala.framework.renderer.Renderer;

/**
 * Der RaytraceVisitor traversiert den {@link org.amcgala.framework.scenegraph.SceneGraph} und berechnet die
 * Schnittpunkte den Objekten innerhalb der Szene.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class Raytracer {

    private Scene scene;
    private Tracer tracer;
    private ViewPlane viewPlane;
    private Vector3d eye;

    public Raytracer() {
        tracer = new RecursiveTracer(5);
        viewPlane = new ViewPlane(Framework.getInstance().getWidth(), Framework.getInstance().getHeight(), 1);
        viewPlane.setSampler(new RandomSampler(Integer.parseInt(Framework.getInstance().getProperties().getProperty("raytracer.sampling"))));
        eye = new Vector3d(0, 0, 600);
    }

    public void setScene(Scene scene) {
        this.scene = scene;

    }

    public void setRenderer(Renderer renderer) {
        viewPlane.setRenderer(renderer);
    }

    public void traceScene() {
        for (int row = 0; row < viewPlane.getVerticalResolution(); row++) {
            for (int column = 0; column < viewPlane.getHorizontalResolution(); column++) {
                RGBColor color = new RGBColor(0, 0, 0);
                for (int n = 0; n < viewPlane.getNumberOfSamples(); n++) {
                    final Vector3d o = viewPlane.getWorldCoordinates(column, row);
                    final Vector3d d = o.sub(eye);
                    Ray ray = new Ray(o, d);
                    color = color.add(tracer.trace(ray, scene));
                }

                // Normalisieren der Farbe
                color = color.times(1.0f / viewPlane.getNumberOfSamples());

                // Rendering des Pixels
                viewPlane.drawPixel(column, row, color);

            }
        }
    }
}

