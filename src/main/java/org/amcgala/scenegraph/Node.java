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
package org.amcgala.scenegraph;

import com.google.common.base.Objects;
import org.amcgala.animation.Updatable;
import org.amcgala.math.Matrix;
import org.amcgala.scenegraph.transform.Transformation;
import org.amcgala.scenegraph.transform.Translation;
import org.amcgala.scenegraph.visitor.Visitor;
import org.amcgala.shape.Shape;
import org.amcgala.shape.util.bounds.BoundingBox;
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
 *
 * @author Robert Giacinto
 * @since 1.0
 */
public final class Node implements Updatable {

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    /**
     * Das Label dieses Knotens. Über diesen lässt sich der Knoten bestimmen und kann dazu verwendet werden,
     * auf den Knoten zur Laufzeit wieder zugreifen zu können.
     */
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
     * Die Transformationen, die an diesem Knoten hängen und sich auf die {@link Shape} Objekte
     * des Knotens und aller Kindsknoten auswirkt.
     */
    private List<Transformation> transformations;

    /**
     * Die BoundingBox, die um die Shapes dieses und aller Kindsknoten aufgespannt wird.
     */
    private BoundingBox boundingBox;


    /**
     * Erstellt eine neue Node mit einem Label, über das die Node innerhalb des
     * Graphens gefunden werden kann.
     *
     * @param label das Label des Knotens
     */
    public Node(String label) {
        this.label = label;
        transformations = new ArrayList<Transformation>();
        transformations.add(new Translation(0, 0, 0));
        shapes = new CopyOnWriteArrayList<Shape>();
        children = new CopyOnWriteArrayList<Node>();
        boundingBox = new BoundingBox();
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
     * @return true, wenn Knoten gefunden und entfernt wurde
     */
    protected boolean remove(Node node) {
        checkArgument(children.contains(node), "Node mit Label " + node.getLabel() + " konnte nicht gefunden werden.");
        return children.remove(node);
    }

    /**
     * Entfernt ein Shape aus diesem Knoten.
     *
     * @param shape das Shape, das entfernt werden soll
     * @return {@code true}, wenn Shape entfern wurde
     */
    protected boolean remove(Shape shape) {
        checkArgument(shapes.contains(shape), "Shape mit Label " + shape.getLabel() + " konnte nicht gefunden werden.");
        return shapes.remove(shape);
    }


    /**
     * Fügt ein neues Geometrieobjekt dieser Node hinzu.
     *
     * @param shape das neue Objekt
     * @return {@code true}, wenn es erfolgreich hinzugefügt wurde
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
     * @return true, wenn Knoten gefunden wurde
     */
    public Node getNode(String label) {
        synchronized (children) {
            if (this.label.equalsIgnoreCase(label)) {
                return this;
            } else {
                for (Node n : children) {
                    n.getNode(label);
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
     * Die zurückgegebene ist read-only. Um einen neuen Kindsknoten hinzuzufügen sollte die entsprochende Methode {@code addNode} verwendet werden.
     *
     * @return die Kindsknoten
     */
    public Collection<Node> getChildNodes() {
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Gibt alle Kindsknoten zurück, die an diesem Knoten hängen.
     *
     * @return die Liste der Kindsknoten
     */
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
     * Fügt dem Knoten eine beliebige Anzahl von Transformationen hinzu.
     * Sollten bereits Transformationen vorhanden sein, so werden die neuen hinten angehängt.
     *
     * @param transformations die Transformationen, die hinzugefügt werden sollen
     */
    public void add(Transformation... transformations) {
        Collections.addAll(this.transformations, transformations);
        log.info("Neue Transformation hinzugefügt: {}", transformations);
    }

    /**
     * Gibt die gesamte Transformationsmatrix zurück.
     *
     * @return die Transformationsmatrix dieses Knotens
     */
    public Matrix getTransformMatrix() {
        Matrix matrix = Matrix.identity(4, 4);

        for (Transformation t : transformations) {
            Matrix tmp = t.getTransformMatrix();
            if (tmp != null) {
                matrix = matrix.times(tmp);
            }
        }

        if (parent != null) {
            Matrix parentTransformMatrix = parent.getTransformMatrix();
            matrix = parentTransformMatrix.times(matrix);
        }

        return matrix;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("label", label).toString();
    }

    @Override
    public void update() {
        for (Transformation t : transformations) {
            t.update();
        }
        Matrix transform = getTransformMatrix();
        for (Shape shape : shapes) {
            shape.update();
            shape.updateBoundingBox(transform);
        }
    }
}
