/*
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.amcgala.framework.shape;

import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.raytracer.Hittable;
import org.amcgala.framework.renderer.Renderable;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.util.bounds.BoundingBox;

import java.awt.Color;

/**
 * Schnittstelle aller Shapes, die im Framework dargestellt werden können.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public interface Shape extends Updatable, Renderable, Hittable {

    /**
     * Setzt die Animation, die auf das Shape angewendet werden soll.
     *
     * @param animation die Animation
     */
    void setAnimation(Animation animation);

    /**
     * Gibt die Animation zurück, die in dem Shape registriert ist.
     *
     * @return die aktuelle Animation
     */
    Animation getAnimation();

    /**
     * Gibt die Farbe des Shapes zurück.
     *
     * @return die Farbe des Shapes
     */
    Color getColor();

    /**
     * Ändert die Farbe des Shapes.
     *
     * @param color die neue Farbe des Shapes
     */
    void setColor(Color color);

    /**
     * Gibt das Label des Shapes zurück.
     *
     * @return das Label des Shapes
     */
    String getLabel();

    /**
     * Gibt den Knoten zurück, an dem das Shapeobjekt angehängt ist.
     *
     * @return der Knoten, der das Shapeobjekt hält
     */
    Node getNode();

    /**
     * Ändert den Knoten, dem das Shapeobjekt zugeordnet ist.
     *
     * @param node der neue Knoten
     */
    void setNode(Node node);

    /**
     * Gibt die {@link BoundingBox} des Shapes zurück.
     *
     * @return die BoundingBox
     */
    BoundingBox getBoundingBox();

    /**
     * Aktualisiert die {@link BoundingBox} eines Shapes durch Anwendung aller Transformationen aus dem {@link org.amcgala.framework.scenegraph.SceneGraph},
     * die sich auf dieses Shape auswirken.
     *
     * @param transform die Transformationsmatrix
     */
    void updateBoundingBox(Matrix transform);

    /**
     * Gibt die {@link Appearance} des Objekts zurück.
     *
     * @return die Appearance des Objekts
     */
    Appearance getAppearance();

    /**
     * Ändert die {@link Appearance} des Objekts.
     *
     * @param appearance die neue Appearance
     */
    void setAppearance(Appearance appearance);
}
