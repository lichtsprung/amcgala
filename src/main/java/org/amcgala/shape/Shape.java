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
package org.amcgala.shape;

import org.amcgala.animation.Animation;
import org.amcgala.animation.Updatable;
import org.amcgala.raytracer.Hittable;
import org.amcgala.raytracer.RGBColor;
import org.amcgala.raytracer.material.Material;
import org.amcgala.renderer.Renderable;
import org.amcgala.scenegraph.Node;

import java.awt.*;

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

    void setColor(RGBColor color);

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
     * Gibt das Material, das zur Färbung des Shapes verwendet wird, zurück
     *
     * @return das Material
     */
    Material getMaterial();

    /**
     * Ändert das Material, das für das Einfärben des Shapes verantwortlich ist.
     *
     * @param material das neue Material
     */
    void setMaterial(Material material);
}
