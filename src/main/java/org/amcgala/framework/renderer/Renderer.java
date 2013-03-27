package org.amcgala.framework.renderer;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;

import javax.swing.*;
import java.awt.*;

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

    Camera getCamera();

    void setCamera(Camera camera);

    Matrix getTransformationMatrix();

    void setTransformationMatrix(Matrix transformationMatrix);

    /**
     * Diese Methode stellt einen Pixel über den DefaultRenderer auf der Ausgabe dar.
     * Die genaue Art und Weise wie der Pixel dargestellt wird, hängt von der
     * jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     * @deprecated wird nach Umstellung auf DisplayLists nicht mehr benötigt
     */
    @Deprecated
    void drawPixel(Pixel pixel);

    /**
     * Diese Methode stellt einen Pixel in einer bestimmten Farbe über den
     * DefaultRenderer auf der Ausgabe dar. Die genaue Art und Weise wie der Pixel
     * dargestellt wird, hängt von der jeweiligen Implementierung ab.
     *
     * @param pixel der Pixel, der dargestellt werden soll
     * @param color die Farbe des Pixels
     */
    @Deprecated
    void drawPixel(Pixel pixel, Color color);

    /**
     * Gibt den Pixel zu einem Vector zurück.
     *
     * @param vector der Vector
     * @return der Pixel auf dem Bildschirm
     */
    @Deprecated
    Pixel getPixel(Vector3d vector);

    /**
     * Zeichnet eine Linie von einem Startpunkt (x1, y1) zu einem Endpunkt (x2, y2).
     *
     * @param x1 der x1 Wert
     * @param y1 der y1 Wert
     * @param x2 der x2 Wert
     * @param y2 der y2 Wert
     */
    @Deprecated
    void drawLine(int x1, int y1, int x2, int y2);

    @Deprecated
    void drawCircle(double x, double y, double radius);

    /**
     * Weist den DefaultRenderer an, den Buffer auszugeben.
     */
    void show();

    @Deprecated
    void drawLine(Vector3d start, Vector3d end);

    @Deprecated
    void drawCircle(Vector3d pos, double radius);

    @Deprecated
    void drawPixel(Vector3d point, Color color);

    void setFrame(JFrame frame);

    @Deprecated
    void fillRect(Pixel pos, int width, int height, Color color);

    Color getColor();

    /**
     * Setzt eine neue Farbe, mit der die weiteren Zeichenbefehle ausgeführt werden.
     *
     * @param color die neue Farbe
     */
    void setColor(Color color);

}
