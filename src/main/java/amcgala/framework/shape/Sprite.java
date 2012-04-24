package amcgala.framework.shape;

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

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Color;
import amcgala.framework.renderer.Pixel;
import amcgala.framework.renderer.Renderer;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Spriteobjekt zum Darstellen
 *
 * @author Steffen Troester
 */
public class Sprite extends Shape {
    /**
     * Position des Sprites
     */
    private double x, y;
    /**
     * Groesse des Sprites
     */
    private int width, height;
    /**
     * Pixelarray
     */
    private Pixel[] pixel;
    private Color[] color;
    /**
     * geoeffnete Datei
     */
    private String filepath;

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param filepath
     * @param x
     * @param y
     * @throws IOException
     */
    public Sprite(String filepath, double x, double y) throws IOException {
        this(filepath);
        this.x = x;
        this.y = y;

    }

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param inputStream
     * @throws IOException
     */
    public void loadImage(InputStream inputStream) throws IOException {

        // Imagefile auslesen
        BufferedImage image = ImageIO.read(inputStream);
        // Groesse definieren
        width = image.getWidth();
        height = image.getHeight();
        // Farbwerte auslesen
        int[] rgbs = new int[width * height];
        image.getRGB(0, 0, width, height, rgbs, 0, width);
        // Pixel erzeugen (Point2d's)
        pixel = new Pixel[rgbs.length];
        color = new Color[rgbs.length];
        for (int i = 0; i < width * height; i++) {
            pixel[i] = new Pixel(i % width, (height - i) / width);


            // Farbwerte auswerfen (Shiftenaufgrund von RGBintValues)
            int red = (rgbs[i] >> 16) & 0xFF;
            int green = (rgbs[i] >> 8) & 0xFF;
            int blue = (rgbs[i]) & 0xFF;

            color[i] = new Color(red, green, blue);
        }

    }

    /**
     * Spriteobjekt aus einer Datei (jpeg,png,gif)
     *
     * @param inputStream
     * @param x
     * @param y
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
     * @param filepath
     * @throws IOException
     */
    public Sprite(String filepath) throws IOException {
        this.filepath = filepath;
        FileInputStream f = new FileInputStream(filepath);
        loadImage(f);
    }

    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        for (int i = 0; i < pixel.length; i++) {
            pixel[i].x = (int) (i % width + x);
            pixel[i].y = (int) ((height - i) / width + y);
            renderer.putPixel(pixel[i], color[i]);
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
        return "Sprite from:" + filepath + " width:" + width + " height:"
                + height;
    }
    
    private static final Logger log = LoggerFactory.getLogger(Sprite.class.getName());
}
