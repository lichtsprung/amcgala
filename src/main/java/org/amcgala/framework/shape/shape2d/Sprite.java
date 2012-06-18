package org.amcgala.framework.shape.shape2d;

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

import com.google.common.base.Objects;
import org.amcgala.framework.renderer.Pixel;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Spriteobjekt zum Darstellen
 *
 * @author Steffen Troester
 */
public class Sprite extends AbstractShape {
    private static final Logger log = LoggerFactory.getLogger(Sprite.class.getName());

    private double x, y;
    private int width, height;
    private Pixel[] pixels;
    private String path;

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param path
     * @param x
     * @param y
     *
     * @throws IOException
     */
    public Sprite(String path, double x, double y) throws IOException {
        this(path);
        this.x = x;
        this.y = y;
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

        width = checkNotNull(image).getWidth();
        height = checkNotNull(image).getHeight();

        // Farbwerte auslesen
        int[] colorValues = new int[width * height];
        image.getRGB(0, 0, width, height, colorValues, 0, width);

        // Pixel erzeugen (Point2d's)
        pixels = new Pixel[colorValues.length];

        for (int i = 0; i < width * height; i++) {
            pixels[i] = new Pixel(i % width + x, (height - i) / width + y, new Color(colorValues[i]));
        }
    }

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param inputStream
     * @param x
     * @param y
     *
     * @throws IOException
     */
    public Sprite(InputStream inputStream, int x, int y) throws IOException {
        loadImage(inputStream);
        this.x = x;
        this.y = y;
    }

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param path
     *
     * @throws IOException
     */
    public Sprite(String path) {

        this.path = checkNotNull(path);
        FileInputStream f = null;
        try {
            f = new FileInputStream(path);
            loadImage(f);
        } catch (FileNotFoundException e) {
            log.error("Datei {} konnte nicht geladen werden!", path);
        }
    }

    @Override
    public void render(Renderer renderer) {
        for (Pixel pixel : pixels) {
            renderer.putPixel(pixel);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Path", path).add("x", x).add("y", y).toString();
    }
}
