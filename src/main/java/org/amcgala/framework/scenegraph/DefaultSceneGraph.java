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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Szenengraph des Frameworks.
 */
public class DefaultSceneGraph implements SceneGraph {

    private Node root;
    private Map<String, Node> nodes;
    private Map<String, Shape> shapes;

    public DefaultSceneGraph() {
        root = new Node("root");
        nodes = new HashMap<String, Node>();
        shapes = new HashMap<String, Shape>();

        nodes.put(root.getLabel(), root);
    }

    @Override
    public void add(Node node) {
        root.add(checkNotNull(node));
        nodes.put(node.getLabel(), node);
    }

    @Override
    public void add(Node child, Node parent) {
        checkArgument(nodes.containsKey(parent.getLabel()), "Elternknoten konnte im Szenengraph nicht gefunden werden");
        parent.add(child);
        nodes.put(child.getLabel(), child);
    }

    @Override
    public void add(Node child, String parentLabel){
        checkArgument(nodes.containsKey(parentLabel), "Elternknoten konnte im Szenengraph nicht gefunden werden");
        Node parent = nodes.get(parentLabel);
        parent.add(child);
        nodes.put(child.getLabel(), child);
    }

    @Override
    public void add(Shape shape, String nodeLabel) {
        checkArgument(nodes.containsKey(nodeLabel), "Knoten konnte im Szenengraph nicht gefunden werden");
        Node node = nodes.get(nodeLabel);
        node.add(shape);
        shapes.put(shape.getLabel(), shape);
    }

    @Override
    public void add(Shape shape, Node node) {
        checkArgument(nodes.containsKey(node.getLabel()), "Knoten konnte im Szenengraph nicht gefunden werden");
        node.add(shape);
    }



    @Override
    public void remove(Node node) {
        checkArgument(checkNotNull(node).getParent() != null, "Root-Knoten darf nicht gelöscht werden!");
        Node parent = node.getParent();
        Collection<Node> children = node.getAllChildren();

        parent.removeNode(node);
        children.add(node);

        for (Node n : children) {
            for (Shape s : n.getShapes()) {
                shapes.remove(s.getLabel());
            }
            nodes.remove(n.getLabel());
        }
        System.out.println(children.size() + " Knoten entfernt.");
    }


    @Override
    public Node getNode(String label) {
        return nodes.get(label);
    }


    @Override
    public void add(Shape shape) {
    }

    @Override
    public void accept(Visitor visitor) {
        root.accept(visitor);
    }

    @Override
    public void add(Transformation transformation) {
        root.setTransformation(transformation);
    }

    @Override
    public void add(Transformation transformation, String label) {
        nodes.get(label).setTransformation(transformation);
    }

    @Override
    public Shape getShape(String label) {
        return shapes.get(label);
    }


}
