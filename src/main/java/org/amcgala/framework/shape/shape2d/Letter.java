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
package org.amcgala.framework.shape.shape2d;

import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Ein Zeichen dass an der Stelle (x,y) dargestellt wird. Beim ersten Verwenden
 * wird hier die Grafik in fontResource geladen und spaeter statisch genutzt.
 *
 * @author Robert Giacinto, Steffen Troester
 */
public class Letter extends AbstractShape {
    private static final String AMCGALA_FONT_GIF = "org/amcgala/font.gif";
    private ArrayList<Point2d> point2ds;
    private static int[] fontResources = null;
    private static final Logger log = LoggerFactory.getLogger(Letter.class);
    private double x;
    private double y;
    private final char letter;

    /**
     * Ein Zeichen dass an der Stelle (x,y) dargestellt wird. Beim ersten
     * Verwenden wird hier die Grafik in fontResource geladen und spaeter
     * statisch genutzt.
     *
     * @param x      Position
     * @param y      Position
     * @param letter Das Zeichen
     */
    public Letter(double x, double y, char letter) {
        this.x = x;
        this.y = y;
        this.letter = letter;
        // initialisieren falls notwendig
        if (fontResources == null) {
            try {
                initLetters();
                log.info("Successful loading Font Resources!");
            } catch (IOException e) {
                log.error("Error loading Font Resources!");
            }
        }
        // Letter lokalisieren
        point2ds = new ArrayList<Point2d>();
        for (int i = 0; i < 16 * 16; i++) {
            // Position jedes Pixels
            int index = i % 16 + (letter % 16 * 16) + (letter / 16 * 256 * 16)
                    + (i / 16 * 256);
            // Farbwerte auswerfen (Shiftenaufgrund von RGB-int-Values)
            int red = (fontResources[index] >> 16) & 0xFF;
            int green = (fontResources[index] >> 8) & 0xFF;
            int blue = (fontResources[index]) & 0xFF;
            // Farbe definieren (Grafik also austauschbar !)
            Color c = new Color(red, green, blue);
            Point2d pixel = new Point2d(i % 16 + x, 16 - (i / 16) + y, c);
            point2ds.add(pixel);
        }
    }

    @Override
    public void render(Renderer renderer) {
        Color old = renderer.getColor();
        if (point2ds != null) {
            for (Point2d p : point2ds) {
                p.render(renderer);
            }
        }
        renderer.setColor(old);
    }

    /**
     * Initialisiert die statische Grafik
     *
     * @throws IOException
     */
    private static void initLetters() throws IOException {
        // Datei öffnen
        InputStream f = ClassLoader.getSystemResourceAsStream(AMCGALA_FONT_GIF);

        // Imagefile auslesen
        BufferedImage image = ImageIO.read(f);
        // Groesse definieren
        int width = image.getWidth();
        int height = image.getHeight();
        fontResources = new int[width * height];
        // Pixel beziehen
        image.getRGB(0, 0, width, height, fontResources, 0, width);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public char getLetter() {
        return letter;
    }
}
