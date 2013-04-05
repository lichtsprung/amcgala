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


    /**
     * Weist den DefaultRenderer an, den Buffer auszugeben.
     */
    void show();


    Color getColor();

    /**
     * Setzt eine neue Farbe, mit der die weiteren Zeichenbefehle ausgeführt werden.
     *
     * @param color die neue Farbe
     */
    void setColor(Color color);

}
