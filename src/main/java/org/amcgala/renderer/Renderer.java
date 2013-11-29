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
    void render();

}
