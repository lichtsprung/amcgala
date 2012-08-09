package org.amcgala.framework.renderer;

import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;

import javax.swing.JFrame;
import java.awt.Color;
import java.util.List;

/**
 * Dieses Interface, welche Methoden ein Renderer zur Verfügung stellen muss, um vom Framework benutzt werden zu können.
 * @author Robert Giacinto
 */
public interface Renderer {
    /**
     * Gibt die Breite der Ausgabe zurück.
     *
     * @return die Breite der Ausgabe
     */
    int getWidth();

    /**
     * Gibt die Höhe der Ausgabe zurück.
     *
     * @return die Höhe der Ausgabe
     */
    int getHeight();

    void setCamera(Camera camera);

    void setTransformationMatrix(Matrix transformationMatrix);

    void setLights(List<Light> lights);

    Camera getCamera();

    Matrix getTransformationMatrix();

    /**
     * Diese Methode stellt einen Pixel über den DefaultRenderer auf der Ausgabe dar.
     * Die genaue Art und Weise wie der Pixel dargestellt wird, hängt von der
     * jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     */
    void drawPixel(Pixel pixel);

    /**
     * Diese Methode stellt einen Pixel in einer bestimmten Farbe über den
     * DefaultRenderer auf der Ausgabe dar. Die genaue Art und Weise wie der Pixel
     * dargestellt wird, hängt von der jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     * @param color die Farbe des Pixels
     */
    void drawPixel(Pixel pixel, Color color);

    /**
     * Gibt den Pixel zu einem Vector zurück.
     * @param vector der Vector
     * @return der Pixel auf dem Bildschirm
     */
    Pixel getPixel(Vector3d vector);

    /**
     * Setzt eine neue Farbe, mit der die weiteren Zeichenbefehle ausgeführt werden.
     *
     * @param color die neue Farbe
     */
    void setColor(Color color);

    /**
     * Zeichnet eine Linie von einem Startpunkt (x1, y1) zu einem Endpunkt (x2, y2).
     *
     * @param x1 der x1 Wert
     * @param y1 der y1 Wert
     * @param x2 der x2 Wert
     * @param y2 der y2 Wert
     */
    void drawLine(int x1, int y1, int x2, int y2);

    void drawCircle(double x, double y, double radius);

    /**
     * Weist den DefaultRenderer an, den Buffer auszugeben.
     */
    void show();

    void drawLine(Vector3d start, Vector3d end);

    void drawCircle(Vector3d pos, double radius);

    void drawPixel(Vector3d point, Color color);

    void drawPixel(Vector3d vector, Appearance appearance);

    void setFrame(JFrame frame);

}
