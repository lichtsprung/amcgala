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
package org.amcgala.framework.scenegraph;

import org.amcgala.framework.scenegraph.transform.Transformation;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.amcgala.framework.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Szenengraph des Frameworks.
 */
public class SceneGraph {

    private Node root;
    private Map<String, Node> namedNodes;
    private Map<String, Shape> namedShapes;
    private List<Node> nodes;
    private List<Shape> shapes;

    public SceneGraph() {
        root = new Node("root");
        namedNodes = new HashMap<String, Node>();
        namedShapes = new HashMap<String, Shape>();
        nodes = new ArrayList<Node>();
        shapes = new ArrayList<Shape>();

        namedNodes.put(root.getLabel(), root);
        nodes.add(root);
    }

    /**
     * Fügt dem Szenengraph einen Knoten hinzu.
     * Dieser wird direkt an den Wurzelknoten des Graphen gehängt.
     *
     * @param node der neue Knoten
     */
    public void add(Node node) {
        root.add(checkNotNull(node));
        nodes.add(node);
    }

    /**
     * Fügt dem Knoten mit der übergebenen Bezeichnung einen neuen Kindsknoten hinzu.
     *
     * @param node  der Kindsknoten, der hinzugefügt werden soll
     * @param label der Name des Knoten, an den der Kindsknoten gehängt werden soll
     */
    public void add(Node node, String label) {
        checkArgument(namedNodes.containsKey(label), "Key nicht im Szenengraph vorhanden!");
        addNamedNode(checkNotNull(node), label);
    }

    /**
     * Entfernt den Knoten aus dem Szenengraph.
     *
     * @param node der Knoten,der entfernt werden soll
     */
    public void remove(Node node) {
        root.removeNode(checkNotNull(node).getLabel());
    }

    /**
     * Entfernt den Knoten mit der übergebenen Bezeichnung aus dem Szenengraph.
     *
     * @param label der Name des Knotens, der entfernt werden soll
     */
    public void remove(String label) {
        root.removeNode(checkNotNull(label));
    }

    /**
     * Gibt den Knoten mit einem gegebenen Namen zurück.
     *
     * @param label der Name des gesuchten Knotens
     *
     * @return der gesuchte Knoten
     */
    public Node getNode(String label) {
        return namedNodes.get(label);
    }

    /**
     * Fügt einem bestimmten Knoten innerhalb des Szenengraphs ein neues Shapeobjekt hinzu.
     *
     * @param label der Name des Knotens
     * @param shape das Shapeobjekt
     */
    public void add(Shape shape, String label) {
        namedNodes.get(label).add(shape);
    }

    /**
     * Fügt dem Wurzelknoten ein neues Shapeobjekt hinzu.
     *
     * @param shape das Shapeobjekt
     */
    public void add(Shape shape) {
        add(shape, "root");
    }

    /**
     * Teil des Visitor-Patterns. Eintrittspunkt der Visitors
     *
     * @param visitor der Visitor
     */
    public void accept(Visitor visitor) {
        root.accept(visitor);
    }

    /**
     * Fügt dem Wurzelknoten eine neue Transformation hinzu.
     *
     * @param transformation die neue Transformation
     */
    public void add(Transformation transformation) {
        root.setTransformation(transformation);
    }

    /**
     * Fügt einem bestimmten Knoten innerhalb des Szenengraphs eine neue Transformation hinzu.
     *
     * @param transformation die neue Transformation
     * @param label          die Bezeichnung des gesuchten Knotens
     */
    public void add(Transformation transformation, String label) {
        namedNodes.get(label).setTransformation(transformation);
    }

    public Shape getShape(String label) {
        return namedShapes.get(label);
    }

    private void addNamedNode(Node node, String label) {
        namedNodes.get(label).add(node);
        nodes.add(node);
    }

    private void removeNamedNode(Node node, String label) {
        namedNodes.remove(label);
        nodes.remove(node);
    }
}
