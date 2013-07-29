package org.amcgala.renderer;

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


    /**
     * Gibt die aktuelle Farbe zurück, die vom Renderer verwendet wird.
     *
     * @return die aktive Farbe
     */
    Color getColor();

    /**
     * Setzt eine neue Farbe, mit der die weiteren Zeichenbefehle ausgeführt werden.
     *
     * @param color die neue Farbe
     */
    void setColor(Color color);

}
