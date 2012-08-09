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

import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wird von jedem DefaultRenderer erweitert und stellt die Funktionen drawPixel und show
 * zur Verfügung.
 */
public class DefaultRenderer implements Renderer {

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
    private List<Light> lights;

    public DefaultRenderer(Camera camera) {
        this.camera = checkNotNull(camera);
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setCamera(Camera camera) {
        this.camera = checkNotNull(camera);
    }

    @Override
    public void setTransformationMatrix(Matrix transformationMatrix) {
        this.transformationMatrix = checkNotNull(transformationMatrix);
    }

    @Override
    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    @Override
    public void drawPixel(Pixel pixel) {
        checkNotNull(pixel);
        g.setColor(pixel.color);
        g.fillRect(offsetX + pixel.x, -pixel.y + offsetY, 1, 1);
    }

    @Override
    public void drawPixel(Pixel pixel, Color color) {
        checkNotNull(pixel);
        checkNotNull(color);
        setColor(color);
        g.fillRect(offsetX + pixel.x, -pixel.y + offsetY, 1, 1);
    }

    @Override
    public Pixel getPixel(Vector3d vector) {
        Vector3d tv = checkNotNull(vector).transform(transformationMatrix);
        return camera.getImageSpaceCoordinates(tv);
    }

    @Override
    public void setColor(Color color) {
        g.setColor(checkNotNull(color));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(offsetX + x1, -y1 + offsetY, offsetX + x2, -y2 + offsetY);
    }

    @Override
    public void drawCircle(double x, double y, double radius) {
        int r2 = (int) (radius);
        int xi = (int) Math.round(x);
        int yi = (int) Math.round(y);
        g.drawOval(offsetX + xi + r2, -yi - r2 + offsetY, r2, r2);
    }

    @Override
    public void show() {
        bs.show();
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    @Override
    public void drawLine(Vector3d start, Vector3d end) {
        Vector3d s = checkNotNull(start).transform(transformationMatrix);
        Vector3d e = checkNotNull(end).transform(transformationMatrix);
        Pixel sp = camera.getImageSpaceCoordinates(s);
        Pixel ep = camera.getImageSpaceCoordinates(e);
        drawLine(sp.x, sp.y, ep.x, ep.y);
    }

    @Override
    public void drawCircle(Vector3d pos, double radius) {
        Vector3d tv = checkNotNull(pos).transform(transformationMatrix);
        Pixel p = camera.getImageSpaceCoordinates(tv);
        drawCircle(p.x, p.y, radius);
    }

    @Override
    public void drawPixel(Vector3d point, Color color) {
        Vector3d tv = checkNotNull(point).transform(transformationMatrix);
        Pixel p = camera.getImageSpaceCoordinates(tv);
        drawPixel(p, checkNotNull(color));
    }

    @Override
    public void drawPixel(Vector3d vector, Appearance appearance) {
        // TODO muss implementiert werden, dass die Appearances richtig verwendet werden.
    }

    @Override
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
