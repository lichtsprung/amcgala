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
package org.amcgala.framework.shape.util;

import com.google.common.base.Objects;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.renderer.DisplayList;
import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ein Containerobjekt, das für die Erstellung von zusammengesetzten Shape-Objekten genutzt werden kann.
 *
 * @author Sascha Lemke
 * @author Robert Giacinto
 */
public class CompositeShape extends AbstractShape implements InputHandler {
    private static final Logger log = LoggerFactory.getLogger(CompositeShape.class.getName());
    private List<Line> lines;


    /**
     * Erstellt ein neues Containerobjekt.
     */
    public CompositeShape() {
        lines = new ArrayList<Line>();
    }

    /**
     * Erstellt ein neues Containerobjekt mit den übergebenen Shape-Objekten.
     *
     * @param lines die Shapes, die hinzugefügt werden sollen
     */
    public CompositeShape(Line... lines) {
        this();
        Collections.addAll(this.lines, lines);
    }

    /**
     * Fügt Shapes dem {@code CompositeShape} hinzu.
     *
     * @param lines die shapes, die dem {@code CompositeShape} hinzugefügt werden sollen
     */
    public void add(Line... lines) {
        Collections.addAll(this.lines, lines);
    }

    /**
     * Entfernt das letzte Shape aus dem Container.
     */
    public void removeLast() {
        lines.remove(lines.size() - 1);
    }

    /**
     * Entfernt ein {@link Shape} aus dem {@code CompositeShape} entfernen.
     *
     * @param line das Shape, das entfernt werden soll
     */
    public void remove(Line line) {
        lines.remove(line);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Shapes", lines).toString();
    }

    @Override
    public DisplayList getDisplayList() {
        DisplayList list = new DisplayList();
        for (Shape line : lines) {
            list.add(line.getDisplayList());
        }
        return list;
    }
}
