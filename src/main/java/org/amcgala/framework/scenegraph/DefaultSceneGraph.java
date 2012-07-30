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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Standardimplementierung des Szenengraph des Frameworks.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class DefaultSceneGraph implements SceneGraph {
    private static final Logger log = LoggerFactory.getLogger(DefaultSceneGraph.class);
    private Node root;
    private Map<String, Node> nodes;
    private Map<String, Shape> shapes;

    /**
     * Standardkonstruktor.
     */
    public DefaultSceneGraph() {
        root = new Node("root");
        nodes = new HashMap<String, Node>();
        shapes = new HashMap<String, Shape>();

        nodes.put(root.getLabel(), root);
    }

    @Override
    public void addNode(Node child) {
        root.add(checkNotNull(child));
        nodes.put(child.getLabel(), child);
    }

    @Override
    public void addNode(Node child, Node parent) {
        checkArgument(nodes.containsKey(parent.getLabel()), "Elternknoten konnte im Szenengraph nicht gefunden werden");
        parent.add(checkNotNull(child));
        nodes.put(child.getLabel(), child);
    }

    @Override
    public void addNode(Node child, String parentLabel) {
        checkArgument(nodes.containsKey(parentLabel), "Elternknoten konnte im Szenengraph nicht gefunden werden");
        Node parent = nodes.get(parentLabel);
        parent.add(child);
        nodes.put(child.getLabel(), child);
    }

    @Override
    public void addShape(Shape shape, String nodeLabel) {
        checkArgument(nodes.containsKey(nodeLabel), "Knoten konnte im Szenengraph nicht gefunden werden");
        Node node = nodes.get(nodeLabel);
        node.add(shape);
        shape.setNode(node);
        shapes.put(shape.getLabel(), shape);
    }

    @Override
    public void addShape(Shape shape, Node node) {
        node.add(shape);
        shape.setNode(node);
        shapes.put(shape.getLabel(), shape);

        if (!nodes.containsKey(node.getLabel())) {
            nodes.put(node.getLabel(), node);
            root.add(node);
        }
    }


    @Override
    public void removeNode(Node node) {
        checkArgument(checkNotNull(node).getParent() != null, "Root-Knoten darf nicht gelöscht werden!");
        Node parent = node.getParent();
        Collection<Node> children = node.getAllChildren();

        parent.remove(node);
        children.add(node);

        for (Node n : children) {
            for (Shape s : n.getShapes()) {
                shapes.remove(s.getLabel());
            }
            nodes.remove(n.getLabel());
        }
        log.info(children.size() + " Knoten entfernt.");
    }

    @Override
    public void removeNode(String label) {
        checkArgument(!"root".equalsIgnoreCase(label), "Root-Knoten darf nicht gelöscht werden!");
        Node node = nodes.get(label);
        Node parent = node.getParent();
        Collection<Node> children = node.getAllChildren();

        parent.remove(node);
        children.add(node);

        for (Node n : children) {
            for (Shape s : n.getShapes()) {
                shapes.remove(s.getLabel());
            }
            nodes.remove(n.getLabel());
        }
        log.info(children.size() + " Knoten entfernt.");
    }

    @Override
    public void removeShape(Shape shape) {
        shape.getNode().remove(shape);
        shapes.remove(shape.getLabel());
    }


    @Override
    public Node getNode(String label) {
        return nodes.get(label);
    }


    @Override
    public void addShape(Shape shape) {
        root.add(checkNotNull(shape));
        shapes.put(shape.getLabel(), shape);
    }

    @Override
    public void accept(Visitor visitor) {
        root.accept(visitor);
    }

    @Override
    public void removeShape(String label) {
        checkArgument(shapes.containsKey(label), "Shape " + label + " konnte nicht gefunden werden");
        Shape shape = shapes.get(label);
        shape.getNode().remove(shape);
        shapes.remove(label);
    }

    @Override
    public void addTransformation(Transformation... transformations) {
        root.add(transformations);
    }

    @Override
    public void addTransformation(String label, Transformation... transformations) {
        nodes.get(label).add(transformations);
    }

    @Override
    public Shape getShape(String label) {
        return shapes.get(label);
    }

    @Override
    public int getNodeCount() {
        return nodes.size();
    }

    @Override
    public Collection<Shape> getAllShapes() {
        return shapes.values();
    }
}
