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
     * Fügt einem Knoten einen neuen Kindsknoten hinzu. Vom Elternknoten muss nur das
     * Label bekannt sein, um diesem ein neues Kind hinzufügen zu können.
     *
     * @param child       der Kindsknoten, der hinzugefügt werden soll
     * @param parentLabel die Bezeichnung des Elternknotens
     */
    void add(Node child, String parentLabel);

    /**
     * Fügt einem Knoten über seinen Bezeichner ein neues Shapeobjekt hinzu.
     *
     * @param shape     das Shape, das dem Knoten hinzugefügt werden soll
     * @param nodeLabel der Bezeichner des Knoten innerhalb des Szenengraphen
     */
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
     * @param transformations die neuen Transformation
     * @param label           die Bezeichnung des gesuchten Knotens
     */
    void add(String label, Transformation... transformations);

    /**
     * Fügt dem Wurzelknoten eine neue Transformation hinzu.
     *
     * @param transformations die neuen Transformation
     */
    void add(Transformation... transformations);

    /**
     * Entfernt den Knoten aus dem Szenengraph.
     *
     * @param node {@link Node}, das entfernt werden soll
     */
    void remove(Node node);

    /**
     * Entfernt den Knoten mit dem übergebenen Label.
     *
     * @param label der Bezeichner des Knotens
     */
    void removeNode(String label);

    /**
     * Entfernt ein Shape aus dem Szenengraph.
     * @param shape das {@link Shape}, das entfernt werden soll
     */
    void remove(Shape shape);

    /**
     * Gibt den Knoten mit einem gegebenen Namen zurück.
     *
     * @param label der Name des gesuchten Knotens
     *
     * @return der gesuchte Knoten
     */
    Node getNode(String label);


    /**
     * Findet ein {@link org.amcgala.framework.shape.Shape} über seinen Namen und gibt dessen Referenz zurück.
     *
     * @param label der Name des Shapes
     *
     * @return das Shape
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
     *
     * @param label der Name des Shapes, das entfernt werden soll
     */
    void removeShape(String label);

    /**
     * Gibt die Anzahl der Knoten im Szenengraph zurück.
     *
     * @return die Anzahl der Knoten
     */
    int getNodeCount();
}
