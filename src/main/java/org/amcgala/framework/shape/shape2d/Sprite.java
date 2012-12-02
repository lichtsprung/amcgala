/*
 * Copyright 2011-2012 Cologne University of Applied Sciences Licensed under the
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
package org.amcgala.framework.shape.shape2d;


import com.google.common.base.Objects;
import org.amcgala.framework.renderer.Pixel;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Spriteobjekt zum Darstellen
 *
 * @author Steffen Tröster
 */
public class Sprite extends AbstractShape {
    private static final Logger log = LoggerFactory.getLogger(Sprite.class.getName());
    private Pixel[] pixels;
    private String path;

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param path der Pfad zu dem Sprite, das geladen werden soll
     */
    public Sprite(String path) {

        this.path = checkNotNull(path);
        FileInputStream f = null;
        try {
            f = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            log.error("Konnte Datei {} nicht finden", path);
        }

        try {
            loadImage(f);
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (IOException e) {
                log.error("Konnte Datei nicht schließen", e);
            }
        }
    }

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param inputStream
     */
    public void loadImage(InputStream inputStream) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            log.error("Konnte Bild nicht laden!", e);
        }

        int width = checkNotNull(image).getWidth();
        int height = checkNotNull(image).getHeight();

        // Farbwerte auslesen
        int[] colorValues = new int[width * height];
        if (image != null) {
            image.getRGB(0, 0, width, height, colorValues, 0, width);
        } else {
            log.error("Couldn't load image from InputStream.");
        }

        // Pixel erzeugen
        pixels = new Pixel[colorValues.length];

        for (int i = 0; i < width * height; i++) {
            pixels[i] = new Pixel(i % width, (height - i) / width, new Color(colorValues[i]));
        }
    }

    @Override
    public void render(Renderer renderer) {
        for (Pixel pixel : pixels) {
            renderer.drawPixel(pixel);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Path", path).toString();
    }
}
