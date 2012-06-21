package org.amcgala.framework.shape;

import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.renderer.Renderable;
import org.amcgala.framework.scenegraph.Node;

import java.awt.Color;

/**
 * Schnittstelle aller Shapes, die im Framework dargestellt werden können.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public interface Shape extends Updatable, Renderable {

    /**
     * Setzt die Animation, die auf das Shape angewendet werden soll.
     *
     * @param animation die AbstractAnimation
     */
    void setAnimation(Animation animation);

    /**
     * Gibt die Animation zurück, die in dem Shape registriert ist.
     *
     * @return die aktuelle AbstractAnimation
     */
    Animation getAnimation();

    Color getColor();

    void setColor(Color color);

    String getLabel();

    Node getNode();

    void setNode(Node node);
}
