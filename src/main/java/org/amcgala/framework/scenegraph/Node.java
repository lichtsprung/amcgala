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

import com.google.common.base.Objects;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.scenegraph.transform.Transformation;
import org.amcgala.framework.scenegraph.transform.Translation;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Eine Node ist Teil des Scenegraphs und kann beliebig viele Kindsknoten und
 * Geometrieobjekte zugewiesen bekommen.
 */
public final class Node implements Updatable {

    private static final Logger logger = LoggerFactory.getLogger(Node.class);
    private String label = null;
    /**
     * Der übergeordnete Knoten, an dem dieser Knoten hängt. {@code null}, wenn es sich
     * um den Rootknoten handelt.
     */
    private Node parent;
    /**
     * Die Kindsknoten, die an diesem Knoten hängen.
     */
    private final List<Node> children;
    /**
     * Die Geometrieobjekte, die an diesem Knoten hängen und von dem DefaultRenderer
     * dargestellt werden.
     */
    private final List<Shape> shapes;
    /**
     * Ein Transformationsobjekt, das sich auf die Geometrie dieses Knotens und
     * der Kindsknoten auswirkt.
     */
    private Transformation transformation;

    private List<Transformation> transformations;

    /**
     * Erstellt eine neue Node mit einem Label, über das die Node innerhalb des
     * Graphens gefunden werden kann.
     *
     * @param label das Label des Knotens
     */
    public Node(String label) {
        this.label = label;
        transformation = new Translation(0, 0, 0);
        transformations = new ArrayList<Transformation>();
        shapes = new CopyOnWriteArrayList<Shape>();
        children = new CopyOnWriteArrayList<Node>();
    }

    /**
     * Erzeugt eine neue Node mit einem Label und einem zugewiesenen
     * Elternknoten.
     *
     * @param label  das Label des Knotens
     * @param parent der Elternknoten
     */
    public Node(String label, Node parent) {
        this(label);
        this.parent = parent;
        this.parent.add(this);
    }

    /**
     * Fügt dem Knoten einen neuen Kindsknoten hinzu.
     *
     * @param childNode der neue Knoten
     *
     * @return gibt Referenz auf sich selbst zurück um verschachtelte Aufrufe zu
     *         ermöglichen
     */
    protected Node add(Node childNode) {
        childNode.parent = this;
        synchronized (children) {
            children.add(childNode);
        }
        return this;
    }

    /**
     * Entfernt einen Kindsknoten mit einem gegebenen Label.
     *
     * @param node der Knoten, der entfernt werden soll.
     *
     * @return true, wenn Knoten gefunden und entfernt wurde
     */
    protected boolean removeNode(Node node) {
        checkArgument(children.contains(node), "Node mit Label " + node.getLabel() + " konnte nicht gefunden werden.");
        return children.remove(node);
    }

    protected boolean removeShape(Shape shape) {
        checkArgument(shapes.contains(shape), "Shape mit Label " + shape.getLabel() + " konnte nicht gefunden werden.");
        return shapes.remove(shape);
    }


    /**
     * Fügt ein neues Geometrieobjekt dieser Node hinzu.
     *
     * @param shape das neue Objekt
     *
     * @return true, wenn es erfolgreich hinzugefügt wurde
     */
    protected boolean add(Shape shape) {
        synchronized (shapes) {
            shape.setNode(this);
            shapes.add(shape);
        }
        return true;
    }

    /**
     * Gibt einen Knoten mit einem bestimmten Label zurück.
     *
     * @param label Label des Knoten, der gefunden werden soll
     *
     * @return true, wenn Knoten gefunden wurde
     */
    public Node getNode(String label) {
        synchronized (children) {
            if (this.label.equalsIgnoreCase(label)) {
                return this;
            } else {
                for (Node n : children) {
                    return n.getNode(label);
                }
            }
        }
        return null;
    }

    /**
     * Übergibt einen neuen Visitor an den Knoten.
     *
     * @param visitor der Visitor, der den Knoten besuchen soll
     */
    public void accept(Visitor visitor) {
        synchronized (children) {
            visitor.visit(this);
            for (Node n : children) {
                n.accept(visitor);
            }
        }
    }

    /**
     * Gibt das Label des Knoten zurück.
     *
     * @return das Label des Knoten
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gibt den Parentknoten zurück.
     *
     * @return der Parentknoten
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gibt die Kindsknoten zurück.
     * Die zurückgegebene ist read-only. Um einen neuen Kindsknoten hinzuzufügen sollte die entsprochende Methode {@code add} verwendet werden.
     *
     * @return die Kindsknoten
     */
    public Collection<Node> getChildren() {

        return Collections.unmodifiableCollection(children);
    }

    public Collection<Node> getAllChildren() {
        List<Node> nodes = new ArrayList<Node>();
        getAllChildren(this, nodes);
        return nodes;
    }

    private static void getAllChildren(Node node, Collection<Node> children) {
        if (node.children.size() > 0) {
            children.addAll(node.children);
            for (Node child : node.children) {
                getAllChildren(child, children);
            }
        }
    }


    /**
     * Gibt die Geometrieobjekt zurück, die an dem Knoten hängen.
     *
     * @return die Geometrieobjekte
     */
    public Collection<Shape> getShapes() {
        return Collections.unmodifiableCollection(shapes);
    }

    /**
     * Gibt die Transformationsmatrix dieses Knotens zurück.
     *
     * @return die Transformationsmatrix
     */
    public Transformation getTransformation() {
        return transformation;
    }

    /**
     * Setzt die aktuelle Transformationsmatrix dieses Knotens.
     * TODO Die Reihenfolge der Transformation spielt eine Rolle - ist es sinnvoll, das so zu gestalten, dass man nur eine Transformation pro Node zulässt?
     * TODO Durch Varargs ersetzen.
     *
     * @param transformation die neue Transformationsmatrix
     */
    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public void setTransformation(Transformation... transformations) {
        Collections.addAll(this.transformations, transformations);
    }

    /**
     * Gibt die gesamte Transformationsmatrix zurück.
     * TODO die Verwendung von varargs ermöglichen.
     *
     * @return
     */
    public Matrix getTransformMatrix() {
        Matrix b = transformation.getTransformMatrix();

        if (parent != null) {
            Matrix c = parent.getTransformMatrix();
            b = b.times(c);
        }

        return b;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("label", label).toString();
    }

    /**
     * TODO Verwendung von varargs berücksichtigen
     */
    @Override
    public void update() {
        if (transformation != null) {
            transformation.update();
        }

        for (Shape shape : shapes) {
            shape.update();
        }
    }
}
