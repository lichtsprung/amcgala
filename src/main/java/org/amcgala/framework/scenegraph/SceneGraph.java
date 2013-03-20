package org.amcgala.framework.scenegraph;

import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.scenegraph.transform.Transformation;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.amcgala.framework.shape.Shape;

import java.util.Collection;

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
    void addNode(Node node);

    /**
     * Fügt ein AbstractShape einem bestimmten Knoten hinzu.
     *
     * @param shape das AbstractShape, das hinzugefügt werden soll
     * @param node  der Knoten, dem dieses AbstractShape hinzugefügt werden soll
     */
    void addShape(Shape shape, Node node);

    /**
     * Fügt einen Knoten einem anderen als Kindsknoten hinzu.
     *
     * @param child  der Kindsknoten
     * @param parent der übergeordnete Elternknoten
     */
    void addNode(Node child, Node parent);

    /**
     * Fügt einem Knoten einen neuen Kindsknoten hinzu. Vom Elternknoten muss nur das
     * Label bekannt sein, um diesem ein neues Kind hinzufügen zu können.
     *
     * @param child       der Kindsknoten, der hinzugefügt werden soll
     * @param parentLabel die Bezeichnung des Elternknotens
     */
    void addNode(Node child, String parentLabel);

    /**
     * Fügt einem Knoten über seinen Bezeichner ein neues Shapeobjekt hinzu.
     *
     * @param shape     das Shape, das dem Knoten hinzugefügt werden soll
     * @param nodeLabel der Bezeichner des Knoten innerhalb des Szenengraphen
     */
    void addShape(Shape shape, String nodeLabel);

    /**
     * Fügt dem Wurzelknoten ein neues Shapeobjekt hinzu.
     *
     * @param shape das Shapeobjekt
     */
    void addShape(Shape shape);


    /**
     * Fügt einem bestimmten Knoten innerhalb des Szenengraphs eine neue Transformation hinzu.
     *
     * @param transformations die neuen Transformation
     * @param label           die Bezeichnung des gesuchten Knotens
     */
    void addTransformation(String label, Transformation... transformations);

    /**
     * Fügt dem Wurzelknoten eine neue Transformation hinzu.
     *
     * @param transformations die neuen Transformation
     */
    void addTransformation(Transformation... transformations);

    /**
     * Fügt dem Root-Knoten des Szenengraphs ein neues Lichtobjekt hinzu.
     *
     * @param light das neue Lichtobjekt, das hinzugefügt werden soll
     */
    void addLight(Light light);

    /**
     * Fügt einem Knoten ein neues Lichtobjekt hinzu.
     *
     * @param light das neue Lichtobjekt
     * @param node  der Knoten, dem das Licht hinzugefügt werden soll
     */
    void addLight(Light light, Node node);

    /**
     * Fügt einem Knoten eine neues Lichtobjekt hinzu.
     *
     * @param light das Lichtobjekt
     * @param label der Name des Knotens
     */
    void addLight(Light light, String label);

    /**
     * Entfernt den Knoten aus dem Szenengraph.
     *
     * @param node {@link Node}, das entfernt werden soll
     */
    void removeNode(Node node);

    /**
     * Entfernt den Knoten mit dem übergebenen Label.
     *
     * @param label der Bezeichner des Knotens
     */
    void removeNode(String label);

    /**
     * Entfernt ein Shape aus dem Szenengraph.
     *
     * @param shape das {@link Shape}, das entfernt werden soll
     */
    void removeShape(Shape shape);

    /**
     * Gibt den Knoten mit einem gegebenen Namen zurück.
     *
     * @param label der Name des gesuchten Knotens
     * @return der gesuchte Knoten
     */
    Node getNode(String label);


    /**
     * Findet ein {@link org.amcgala.framework.shape.Shape} über seinen Namen und gibt dessen Referenz zurück.
     *
     * @param label der Name des Shapes
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

    void removeLight(String label);

    /**
     * Gibt die Anzahl der Knoten im Szenengraph zurück.
     *
     * @return die Anzahl der Knoten
     */
    int getNodeCount();

    /**
     * Gibt alle {@link Shape} Objekte zurück, die im {@link SceneGraph} verwaltet werden.
     *
     * @return die Liste aller Objekte im Szenengraph
     */
    Collection<Shape> getAllShapes();

    /**
     * Prüft, ob dem Szenengraph Lichter hinzugefügt wurden.
     *
     * @return {@code true}, wenn Lichter im Szenengraph vorhanden sind
     */
    boolean hasLight();

    /**
     * Entfernt alle Shapes, die dem Szenengraph hinzugefügt wurden.
     */
    void removeShapes();
}
