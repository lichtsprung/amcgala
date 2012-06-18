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
package org.amcgala.framework.renderer;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wird von jedem Renderer erweitert und stellt die Funktionen putPixel und show
 * zur Verfügung.
 */
public class Renderer {

    /**
     * Die Breite der Ausgabe des Renderers
     */
    private int width;
    /**
     * Die Höhe der Ausgabe des Renderers
     */
    private int height;
    private BufferStrategy bs;
    private int offsetX;
    private int offsetY;
    private JFrame frame;
    private Graphics g;
    private Camera camera;
    private Matrix transformationMatrix;


    /**
     * Gibt die Breite der Ausgabe zurück.
     *
     * @return die Breite der Ausgabe
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gibt die Höhe der Ausgabe zurück.
     *
     * @return die Höhe der Ausgabe
     */
    public int getHeight() {
        return height;
    }

    public void setCamera(Camera camera) {
        this.camera = checkNotNull(camera);
    }

    public void setTransformationMatrix(Matrix transformationMatrix) {
        this.transformationMatrix = checkNotNull(transformationMatrix);
    }

    public Camera getCamera() {
        return camera;
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    /**
     * Diese Methode stellt einen Pixel über den Renderer auf der Ausgabe dar.
     * Die genaue Art und Weise wie der Pixel dargestellt wird, hängt von der
     * jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     */
    public void putPixel(Pixel pixel) {
        checkNotNull(pixel);
        g.setColor(pixel.color);
        g.fillRect(offsetX + pixel.x, -pixel.y + offsetY, 1, 1);
    }

    /**
     * Diese Methode stellt einen Pixel in einer bestimmten Farbe über den
     * Renderer auf der Ausgabe dar. Die genaue Art und Weise wie der Pixel
     * dargestellt wird, hängt von der jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     * @param color die Farbe des Pixels
     */
    public void putPixel(Pixel pixel, Color color) {
        checkNotNull(pixel);
        checkNotNull(color);
        setColor(color);
        g.fillRect(offsetX + pixel.x, -pixel.y + offsetY, 1, 1);
    }

    /**
     * Setzt eine neue Farbe, mit der die weiteren Zeichenbefehle ausgeführt werden.
     *
     * @param color die neue Farbe
     */
    public void setColor(Color color) {
        g.setColor(checkNotNull(color));
    }

    /**
     * Zeichnet eine Linie von einem Startpunkt (x1, y1) zu einem Endpunkt (x2, y2).
     *
     * @param x1 der x1 Wert
     * @param y1 der y1 Wert
     * @param x2 der x2 Wert
     * @param y2 der y2 Wert
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(offsetX + x1, -y1 + offsetY, offsetX + x2, -y2 + offsetY);
    }

    public void drawCircle(int x, int y, int radius) {
        int r2 = (int) (radius);
        g.drawOval(offsetX + x + r2, -y - r2 + offsetY, r2, r2);
    }

    public void drawCircle(double x, double y, double radius) {
        int r2 = (int) (radius);
        int xi = (int) Math.round(x);
        int yi = (int) Math.round(y);
        g.drawOval(offsetX + xi + r2, -yi - r2 + offsetY, r2, r2);
    }

    /**
     * Weist den Renderer an, den Buffer auszugeben.
     */
    public void show() {
        bs.show();
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    public void drawLine(Vector3d start, Vector3d end) {
        Vector3d s = checkNotNull(start).transform(transformationMatrix);
        Vector3d e = checkNotNull(end).transform(transformationMatrix);
        Pixel sp = camera.getImageSpaceCoordinates(s);
        Pixel ep = camera.getImageSpaceCoordinates(e);
        drawLine(sp.x, sp.y, ep.x, ep.y);
    }

    public void drawCircle(Vector3d pos, double radius) {
        Vector3d tv = checkNotNull(pos).transform(transformationMatrix);
        Pixel p = camera.getImageSpaceCoordinates(tv);
        drawCircle(p.x, p.y, radius);
    }

    public void putPixel(Vector3d point, Color color) {
        Vector3d tv = checkNotNull(point).transform(transformationMatrix);
        Pixel p = camera.getImageSpaceCoordinates(tv);
        putPixel(p, checkNotNull(color));
    }

    public void setFrame(JFrame frame) {
        this.frame = checkNotNull(frame);

        this.width = frame.getWidth();
        this.height = frame.getHeight();

        this.offsetX = frame.getWidth() >> 1;
        this.offsetY = frame.getHeight() >> 1;

        frame.createBufferStrategy(2);
        bs = frame.getBufferStrategy();
        g = bs.getDrawGraphics();
    }
}
