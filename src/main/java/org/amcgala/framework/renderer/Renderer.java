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
 *
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

    /**
     * Ändert die Kamera, mit die der Renderer arbeitet.
     *
     * @param camera die neue Kamera
     */
    void setCamera(Camera camera);

    /**
     * Ändert die Transformationsmatrix, die vom Renderer verwendet wird.
     *
     * @param transformationMatrix die neue Transformationsmatrix
     */
    void setTransformationMatrix(Matrix transformationMatrix);

    /**
     * Ändert die Lichter, die vom Renderer verwendet werden.
     *
     * @param lights die Lichtobjekte
     */
    void setLights(List<Light> lights);

    /**
     * Gibt die verwendete Kamera zurück.
     *
     * @return die Kamera
     */
    Camera getCamera();

    /**
     * Gibt die Transformationsmatrix zurück.
     *
     * @return die Transformationsmatrix
     */
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
     *
     * @param vector der Vector
     *
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

    /**
     * Zeichnet einen Kreis mit dem Mittelpunkt (x,y).
     *
     * @param x      x-Position des Mittelpunkts
     * @param y      y-Position des Mittelpunkts
     * @param radius der Radius
     */
    void drawCircle(double x, double y, double radius);

    /**
     * Weist den DefaultRenderer an, den Buffer auszugeben.
     */
    void show();

    /**
     * Zeichnet eine Linie zwischen den beiden Vektoren als Start- und Endpunkte.
     *
     * @param start der Startvektor
     * @param end   der Endvektor
     */
    void drawLine(Vector3d start, Vector3d end);

    /**
     * Zeichnet einen Kreis mit dem Mittelpunkt definiert über den Vektor.
     *
     * @param pos    die Position des Mittelpunkts
     * @param radius der Radius
     */
    void drawCircle(Vector3d pos, double radius);

    /**
     * Zeichnet einen Pixel einer bestimmten Farbe.
     *
     * @param point die Position des Pixels
     * @param color die Farbe
     */
    void drawPixel(Vector3d point, Color color);

    /**
     * Zeichnet einen Pixel.
     *
     * @param vector     die Position des Pixels
     * @param appearance die Appearance
     */
    void drawPixel(Vector3d vector, Appearance appearance);

    /**
     * Ändert den Frame, in dem der Renderer zeichnet.
     *
     * @param frame der Frame
     */
    void setFrame(JFrame frame);
}
