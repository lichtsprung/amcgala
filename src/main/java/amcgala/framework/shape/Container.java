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
package amcgala.framework.shape;

import amcgala.framework.camera.Camera;
import amcgala.framework.event.InputHandler;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Zeichnet ein Shapeobjekt mit übergebenen Parametern in den Formen: BresenhamLine oder Vector3D <br />
 * Weiter Linien oder Vektoren können auch im nachhinein hinzugefügt werden.<br />
 * Implementiert außerdem den Eventbus.
 *
 * @author Sascha Lemke
 */
public class Container extends Shape implements InputHandler {

    private ArrayList<BresenhamLine> linien;

    /**
     * Erstellt eine "leeres" Shapeobjekt.
     */
    public Container() {
        this.linien = new ArrayList<BresenhamLine>();
    }

    /**
     * Wird benutzt um ein Shapeobjekt auf Basis eines Arrays zu entwerfen.
     *
     * @param koordinaten
     */
    public Container(Vector3d[][] koordinaten) {
        this.linien = new ArrayList<BresenhamLine>();
        for (int i = 0; i < koordinaten.length; i++) {
            this.linien.add(new BresenhamLine(koordinaten[i][0], koordinaten[i][1]));
        }
    }

    /**
     * Übernimmt automatisch 3D-Linien um sie mit anderen Linien zu verbinden.
     *
     * @param linien
     */
    public Container(BresenhamLine[] linien) {
        this.linien = new ArrayList<BresenhamLine>();
        for (int i = 0; i < linien.length; i++) {
            this.linien.add(linien[i]);
        }
    }

    /**
     * Fügt eine Linie am Ende hinzu.
     *
     * @param point
     * @param point1
     */
    public void add(Vector3d point, Vector3d point1) {
        this.linien.add(new BresenhamLine(point, point1));
    }


    /**
     * Übernimmt die übergebenen Linien und bezieht sie in das ShapeObjekt mit ein.
     *
     * @param lines
     */
    public void add(BresenhamLine[] linien) {
        for (int i = 0; i < linien.length; i++) {
            this.linien.add(linien[i]);
        }
    }

    /**
     * Entfernt den letzten Eintrag.
     *
     * @return
     */
    public void remove() {
        this.linien.remove(this.linien.size() - 1);
    }

    /**
     * Entfernt einen Eintrag an gewählter Stelle.
     *
     * @param index Die Stelle des Knotens der gelöscht werden soll (Achtung! Wir zählen von 0 an!)
     * @return
     */
    public void remove(int index) {
        this.linien.remove(index);
    }

    /**
     * Überschreibt die <i>toString</i> Methode der Klasse <i>Object</i> und gibt bei Aufruf die Linien mit den Koordinaten wieder, sowie die Anzahl der Linen und ob diese Inhalt besitzen.
     */
    @Override
    public String toString() {
        String ausgabe = "";
        Iterator<BresenhamLine> iter = this.linien.iterator();

        int i = 0;
        while (iter.hasNext()) {
            BresenhamLine line = iter.next();
            ausgabe += "" + i + ". Linie: \n";
            ausgabe += "\t x1: " + line.x1 + "\n";
            ausgabe += "\t y1: " + line.y1 + "\n";
            ausgabe += "\t z1: " + line.z1 + "\n\n";

            ausgabe += "\t x1: " + line.x2 + "\n";
            ausgabe += "\t y1: " + line.y2 + "\n";
            ausgabe += "\t z1: " + line.z2 + "\n\n";
            i++;
        }

        ausgabe += "- Linien insgesamt: " + this.linien.size();
        return ausgabe;
    }

    /**
     *
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
        Iterator<BresenhamLine> iter = this.linien.iterator();
        while (iter.hasNext()) {
            BresenhamLine line = iter.next();
            line.color = color;
            line.render(transformation, camera, renderer);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(Container.class.getName());
}
