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
package org.amcgala.example.animation;

import org.amcgala.Framework;
import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.shape.Line;

import java.util.Random;

/**
 * In diesem Beispiel soll demonstriert werden, wie mit der Animation-Klasse ein animiertes Verhalten von Shapes
 * implementiert werden kann.
 * Hierfür soll eine 2d Linie so verändert werden, dass die Endpunkte der Linie einer zufälligen (brownschen) Bewegung folgen
 * und zufällig versetzt werden, wenn sie den Rand der Bildschirmausgabe berühren.
 *
 * @author Robert Giacinto
 */
public class SimpleAnimationMain extends Framework {

    private Line[] lines;
    private Random random;

    /**
     * Erzeugt ein SimpleAnimation Beispiel mit einer bestimmten Größe.
     *
     * @param width  die Breite des Beispielfensters
     * @param height die Höhe des Beispielfensters
     */
    public SimpleAnimationMain(int width, int height) {
        super(width, height);
    }

    @Override
    public void initGraph() {
        lines = new Line[100];
        random = new Random(System.nanoTime());

        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(
                    Math.pow(-1, random.nextInt(2)) * random.nextInt(getScreenWidth() / 2),
                    Math.pow(-1, random.nextInt(2)) * random.nextInt(getScreenHeight() / 2),
                    Math.pow(-1, random.nextInt(2)) * random.nextInt(getScreenWidth() / 2),
                    Math.pow(-1, random.nextInt(2)) * random.nextInt(getScreenWidth() / 2));

            lines[i].setAnimation(new SimpleAnimation());

            add(lines[i]);
        }

    }

    /**
     * Einstiegspunkt für das Beispiel.
     *
     * @param args mögliche Parameter, die über die Konsole übergeben werden
     */
    public static void main(String[] args) {
        Framework framework = new SimpleAnimationMain(800, 600);
        framework.start();
    }

    private class SimpleAnimation extends Animation<Line> {

        @Override
        public void update() {
            /*
             * Erzeugen von vier Zufallswerten zwischen -1 und 1.
             */
            int dx = (int) (Math.pow(-1, random.nextInt(2)) * random.nextInt(2));
            int dy = (int) (Math.pow(-1, random.nextInt(2)) * random.nextInt(2));
            int dx2 = (int) (Math.pow(-1, random.nextInt(2)) * random.nextInt(2));
            int dy2 = (int) (Math.pow(-1, random.nextInt(2)) * random.nextInt(2));

            shape.x1 += dx;
            shape.x2 += dx2;
            shape.y1 += dy;
            shape.y2 += dy2;

            /*
             * Wenn ein Punkt der Linie an den Rand der Anzeige stößt wird dieser zufällig an eine andere Stelle gesetzt. 
             */
            if (shape.x1 < -getScreenWidth() / 2 || shape.x1 > getScreenWidth() / 2) {
                shape.x1 = random.nextInt(getScreenWidth());
            }

            if (shape.x2 < -getScreenWidth() / 2 || shape.x2 > getScreenWidth() / 2) {
                shape.x2 = random.nextInt(getScreenWidth());
            }

            if (shape.y1 < -getScreenWidth() / 2 || shape.y1 > getScreenWidth() / 2) {
                shape.y1 = random.nextInt(getScreenWidth());
            }

            if (shape.y2 < -getScreenWidth() / 2 || shape.y2 > getScreenWidth() / 2) {
                shape.y2 = random.nextInt(getScreenWidth());
            }
        }
    }
}
