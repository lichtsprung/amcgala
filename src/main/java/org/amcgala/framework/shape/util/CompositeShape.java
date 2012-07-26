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
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ein Containerobjekt, das für die Erstellung von zusammengesetzten Shape-Objekten genutzt werden kann.
 * Für ein Beispiel siehe {@link org.amcgala.framework.shape.shape3d.Box}.
 *
 * @author Sascha Lemke
 */
public class CompositeShape extends AbstractShape implements InputHandler {
    private static final Logger log = LoggerFactory.getLogger(CompositeShape.class.getName());
    private List<Shape> shapes;


    /**
     * Erstellt ein neues Containerobjekt.
     */
    public CompositeShape() {
        shapes = new ArrayList<Shape>();
    }

    /**
     * Erstellt ein neues Containerobjekt mit den übergebenen Shape-Objekten.
     *
     * @param shapes die Shapes, die hinzugefügt werden sollen
     */
    public CompositeShape(Shape... shapes) {
        this();
        Collections.addAll(this.shapes, shapes);
    }

    /**
     * Fügt Shapes dem {@code CompositeShape} hinzu.
     *
     * @param shapes die shapes, die dem {@code CompositeShape} hinzugefügt werden sollen
     */
    public void add(Shape... shapes) {
        Collections.addAll(this.shapes, shapes);
    }

    /**
     * Entfernt das letzte Shape aus dem Container.
     */
    public void removeLast() {
        shapes.remove(this.shapes.size() - 1);
    }

    /**
     * Entfernt ein {@link Shape} aus dem {@code CompositeShape} entfernen.
     *
     * @param shape das Shape, das entfernt werden soll
     */
    public void remove(Shape shape) {
        this.shapes.remove(shape);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("Shapes", shapes).toString();
    }

    @Override
    public void render(Renderer renderer) {
        for (Shape shape : shapes) {
            shape.setColor(getColor());
            shape.render(renderer);
        }
    }
}
