package org.amcgala.framework.shape;

import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.math.Matrix;
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
public interface Shape extends Updatable, Renderable {

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
}
