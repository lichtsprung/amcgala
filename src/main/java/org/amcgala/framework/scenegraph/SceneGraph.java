package org.amcgala.framework.scenegraph;

import org.amcgala.framework.scenegraph.transform.Transformation;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.amcgala.framework.shape.Shape;

/**
 * Dieses Interface beschreibt die Methoden, die ein Scene Graph zur Verfügung stellen muss.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public interface SceneGraph {
    /**
     * Fügt dem Szenengraph einen Knoten hinzu.
     * Dieser wird direkt an den Wurzelknoten des Graphen gehängt.
     *
     * @param node der neue Knoten
     */
    void add(Node node);

    /**
     * Fügt ein AbstractShape einem bestimmten Knoten hinzu.
     *
     * @param shape das AbstractShape, das hinzugefügt werden soll
     * @param node  der Knoten, dem dieses AbstractShape hinzugefügt werden soll
     */
    void add(Shape shape, Node node);

    /**
     * Fügt einen Knoten einem anderen als Kindsknoten hinzu.
     *
     * @param child  der Kindsknoten
     * @param parent der übergeordnete Elternknoten
     */
    void add(Node child, Node parent);

    /**
     * @param child
     * @param parentLabel
     */
    void add(Node child, String parentLabel);

    void add(Shape shape, String nodeLabel);

    /**
     * Fügt dem Wurzelknoten ein neues Shapeobjekt hinzu.
     *
     * @param shape das Shapeobjekt
     */
    void add(Shape shape);

    /**
     * Fügt einem bestimmten Knoten innerhalb des Szenengraphs eine neue Transformation hinzu.
     *
     * @param transformation die neue Transformation
     * @param label          die Bezeichnung des gesuchten Knotens
     */
    void add(Transformation transformation, String label);

    /**
     * Fügt dem Wurzelknoten eine neue Transformation hinzu.
     *
     * @param transformation die neue Transformation
     */
    void add(Transformation transformation);

    /**
     * Entfernt den Knoten aus dem Szenengraph.
     *
     * @param node der Knoten,der entfernt werden soll
     */
    void remove(Node node);

    /**
     * Gibt den Knoten mit einem gegebenen Namen zurück.
     *
     * @param label der Name des gesuchten Knotens
     *
     * @return der gesuchte Knoten
     */
    Node getNode(String label);


    /**
     * Findet ein {@link org.amcgala.framework.shape.AbstractShape} über seinen Namen und gibt dessen Referenz zurück.
     *
     * @param label der Name des Shapes
     *
     * @return das AbstractShape
     */
    Shape getShape(String label);

    /**
     * Teil des Visitor-Patterns. Eintrittspunkt der Visitors
     *
     * @param visitor der Visitor
     */
    void accept(Visitor visitor);

    /**
     * Entfernt ein {@link Shape} aus dem Szenengraph.
     * @param label der Name des Shapes, das entfernt werden soll
     */
    void removeShape(String label);
}
