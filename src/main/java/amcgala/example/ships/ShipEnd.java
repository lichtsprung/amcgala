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
package amcgala.example.ships;

import java.util.Collection;

import amcgala.framework.camera.Camera;
import amcgala.framework.lighting.Light;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.BresenhamLine;

/**
 * Ein Schiffsendstück, das zum Zusammensetzen eines Schiffs benutzt werden
 * kann.
 *
 * @author Robert Giacinto
 */
public class ShipEnd extends Ship {

    private static final BresenhamLine[] bottom = {
            new BresenhamLine(0,1, 0, 0.4),
            new BresenhamLine(0, 0.4, 0.4, 0),
            new BresenhamLine(0.4, 0, 0.6, 0),
            new BresenhamLine(0.6, 0, 1, 0.4),
            new BresenhamLine(1, 0.4, 1, 1)
    };
    private static final BresenhamLine[] left = {
            new BresenhamLine(1, 0, 0.4, 0.0),
            new BresenhamLine(0.4, 0, 0, 0.4),
            new BresenhamLine(0, 0.4, 0, 0.6),
            new BresenhamLine(0, 0.6, 0.4, 1),
            new BresenhamLine(0.4, 1, 1, 1)
    };
    private static final BresenhamLine[] right = {
            new BresenhamLine(1 - 1, 0, 1 - 0.4, 0.0),
            new BresenhamLine(1 - 0.4, 0, 1 - 0, 0.4),
            new BresenhamLine(1 - 0, 0.4, 1 - 0, 0.6),
            new BresenhamLine(1 - 0, 0.6, 1 - 0.4, 1),
            new BresenhamLine(1 - 0.4, 1, 1 - 1, 1)
    };
    private static final BresenhamLine[] top = {
            new BresenhamLine(0, 1 - 1, 0, 1 - 0.4),
            new BresenhamLine(0, 1 - 0.4, 0.4, 1 - 0),
            new BresenhamLine(0.4, 1 - 0, 0.6, 1 - 0),
            new BresenhamLine(0.6, 1 - 0, 1, 1 - 0.4),
            new BresenhamLine(1, 1 - 0.4, 1, 1 - 1)
    };

    /**
     * Erstellt eine neue
     * <code>ShipEnd</code> Instanz.
     *
     * @param width   die Breite
     * @param height  die Höhe
     * @param x       die x-Koordinate der Position
     * @param y       die y-Komponente der Position
     * @param heading die Ausrichtung des Schiffs
     */
    public ShipEnd(double width, double height, double x, double y, Heading heading) {
        super(width, height, x, y, heading);
    }

    public ShipEnd(double width, double height, Heading heading) {
        super(width, height, 0, 0, heading);
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        if (Heading.BOTTOM.equals(heading)) {
            for (BresenhamLine line : bottom) {
                line.render(transformation, camera, renderer, lights);
            }
        } else if (Heading.LEFT.equals(heading)) {
            for (BresenhamLine line : left) {
                line.render(transformation, camera, renderer, lights);
            }
        } else if (Heading.RIGHT.equals(heading)) {
            for (BresenhamLine line : right) {
                line.render(transformation, camera, renderer, lights);
            }

        } else if (Heading.TOP.equals(heading)) {
            for (BresenhamLine line : top) {
                line.render(transformation, camera, renderer, lights);
            }
        }
    }
}
