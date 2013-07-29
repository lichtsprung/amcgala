package org.amcgala;

import com.google.common.eventbus.EventBus;
import org.amcgala.camera.Camera;
import org.amcgala.camera.SimplePerspectiveCamera;
import org.amcgala.event.InputHandler;
import org.amcgala.math.Vector3d;
import org.amcgala.raytracer.RGBColor;
import org.amcgala.scenegraph.DefaultSceneGraph;
import org.amcgala.scenegraph.Node;
import org.amcgala.scenegraph.SceneGraph;
import org.amcgala.scenegraph.transform.Transformation;
import org.amcgala.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Ein {@link Scene} Objekt verwaltet alle Objekte und den dazugehörigen {@link org.amcgala.scenegraph.DefaultSceneGraph},
 * der über die Klasse {@link org.amcgala.Framework} dargestellt werden kann.
 * Folgende Objekte werden von jeder Szene selbstständig verwaltet und beim Laden durch das Framework zur Darstellung
 * verwendet:
 * <ul>
 * <li>Ein Szenengraph, der sich um die hierarchische Verwaltung der Szene kümmert. Die Szene bietet dafür Methoden an,
 * die den Umgang mit dem Szenengraph vereinfachen.</li>
 * <li>Eine virtuelle Kamera, die für die Projektion der Szene verwendet wird. Hier können in jeder Szene unterschiedliche
 * Implementierungen verwendet werden.</li>
 * <li>Ein Renderer, die sich um die Darstellung der projezierten Geometrien kümmert. Auch hier können, abhängig von
 * den Anforderungen der jeweiligen Szene, unterschiedliche Implementierungen verwendet werden.</li>
 * <li>Ein Eventbus, der zum Message-Handling zwischen unterschiedlichen Objekten der Szene und zur Reaktion auf Key-
 * oder Mouse-Events verwendet werden kann.</li>
 * </ul>
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Scene {
    private static final Logger log = LoggerFactory.getLogger(Scene.class);
    private SceneGraph sceneGraph;
    private Camera camera;
    private EventBus eventBus;
    private String label;
    private Map<String, InputHandler> inputHandlers;
    private RGBColor background = new RGBColor(0, 0, 0);

    /**
     * Erstellt eine neue Szene mit einem bestimmten Bezeichner.
     *
     * @param label der Bezeichner der Szene
     */
    public Scene(String label) {
        this.label = label;
        sceneGraph = new DefaultSceneGraph();
        camera = new SimplePerspectiveCamera(Vector3d.UNIT_Y, Vector3d.UNIT_Z, Vector3d.ZERO, 2000);
        eventBus = new EventBus();
        inputHandlers = new HashMap<String, InputHandler>();
    }

    /**
     * Fügt das AbstractShape dem Rootknoten des {@link org.amcgala.scenegraph.DefaultSceneGraph} hinzu.
     *
     * @param shape das hinzuzufügende Objekt
     */
    public Scene addShape(Shape shape) {
        sceneGraph.addShape(shape);
        return this;
    }

    /**
     * Fügt dem Rootknoten des {@link org.amcgala.scenegraph.DefaultSceneGraph} ein {@link org.amcgala.scenegraph.Node} hinzu.
     *
     * @param node der neue Knoten
     */
    public Scene addNode(Node node) {
        sceneGraph.addNode(node);
        return this;
    }

    /**
     * Fügt der Szene ein neues Shapeobjekt hinzu. Dieses wird dem Szenengraph an dem übergebenen Knoten angehängt.
     *
     * @param shape das Shape, das der Szene hinzugefügt werden soll
     * @param node  der Knoten, an dem das Shape angehängt werden soll
     */
    public Scene addShape(Shape shape, Node node) {
        sceneGraph.addShape(shape, node);
        return this;
    }

    /**
     * Fügt der Szene ein neues Shapeobjekt hinzu. Es wird dem Knoten mit dem übergebenen Label angehängt.
     *
     * @param shape     das Shape, das der Szene hinzugefügt werden soll
     * @param nodeLabel das Label des Knotens
     */
    public Scene addShape(Shape shape, String nodeLabel) {
        sceneGraph.addShape(shape, nodeLabel);
        return this;
    }

    /**
     * Fügt der Szene einen neuen Knoten hinzu und hängt diesen an den Elternknoten mit dem übergebenen Label.
     *
     * @param node        der neue Knoten, der hinzugefügt werden soll
     * @param parentLabel das Label des Elternknotens
     */
    public Scene addNode(Node node, String parentLabel) {
        sceneGraph.addNode(node, parentLabel);
        return this;
    }

    /**
     * Fügt einem Elternknoten einen neuen Kindsknoten im Szenengraph hinzu.
     *
     * @param child  der neue Kindsknoten
     * @param parent der Elternknoten
     */
    public Scene addNode(Node child, Node parent) {
        sceneGraph.addNode(child, parent);
        return this;
    }

    /**
     * Fügt dem root {@link org.amcgala.scenegraph.Node} des {@link org.amcgala.scenegraph.SceneGraph} eine Transformation hinzu,
     *
     * @param transformation die Transformation, die hinzugefügt werden soll
     */
    public Scene addTransformation(Transformation transformation) {
        sceneGraph.addTransformation(transformation);
        return this;
    }

    /**
     * Gibt die gerade aktive Kamera der Szene zurück.
     *
     * @return die innerhalb der Szene verwendete Kamera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Ändert die von der Szene verwendete {@link org.amcgala.camera.Camera}.
     *
     * @param camera die neue Kamera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Gibt den {@link com.google.common.eventbus.EventBus} der Szene zurück.
     *
     * @return der in der Szene verwendete {@link com.google.common.eventbus.EventBus}
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Gibt das Label der Szene zurück.
     *
     * @return das Label der Szene
     */
    public String getLabel() {
        return label;
    }

    /**
     * Registriert einen neuen Eventhandler bei der EventQueue.
     *
     * @param inputHandler der neue Inputhandler
     * @param label        Name des neuen Inputhandlers
     */
    public Scene addInputHandler(InputHandler inputHandler, String label) {
        inputHandlers.put(label, inputHandler);
        eventBus.register(inputHandler);
        return this;
    }

    /**
     * Entfernt einen Eventhandler aus der Liste der Subscriber.
     *
     * @param label Name des {@code InputHandler} der entfernt werden soll
     */
    public Scene removeInputHandler(String label) {
        checkArgument(inputHandlers.containsKey(label), "InputHandler mit Namen " + label + " konnte nicht gefunden werden");
        eventBus.unregister(inputHandlers.get(label));
        return this;
    }

    /**
     * Entfernt ein Shape aus der Szene.
     *
     * @param label das Label des Shapes, das entfernt werden soll
     */
    public Scene removeShape(String label) {
        sceneGraph.removeShape(label);
        return this;
    }

    /**
     * Entfernt ein Shape aus denen Szenengraph.
     *
     * @param shape das Shape, das entfernt werden soll
     */
    public Scene removeShape(Shape shape) {
        sceneGraph.removeShape(shape);
        return this;
    }

    /**
     * Entfernt einen Knoten aus dem Szenengraph.
     *
     * @param label der Name des Knotens
     */
    public Scene removeNode(String label) {
        sceneGraph.removeNode(label);
        return this;
    }

    /**
     * Entfernt einen Knoten aus dem Szenengraph.
     *
     * @param node der Knoten, der entfernt werden soll
     */
    public Scene removeNode(Node node) {
        sceneGraph.removeNode(node);
        return this;
    }

    /**
     * Gibt den Szenengraph dieser Szene zurück.
     *
     * @return der Szenengraph
     */
    protected SceneGraph getSceneGraph() {
        return sceneGraph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scene scene = (Scene) o;

        return !(label != null ? !label.equals(scene.label) : scene.label != null);
    }

    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    }

    /**
     * Gibt die Hintergrundfarbe der Szene zurück. Diese Methode ist zur Zeit nur für die Raytracing Implementierung
     * relevant.
     *
     * @return die Hintergrundfarbe
     */
    public RGBColor getBackground() {
        return background;
    }

    /**
     * Ändert die Farbe des Szenenhintergrunds.
     *
     * @param background die Farbe des Szenenhintergrunds
     */
    public void setBackground(RGBColor background) {
        this.background = background;
    }

    /**
     * Gibt alle Shapes im Szenengraph zurück.
     *
     * @return alle Shapes im Szenengraph
     */
    public Collection<Shape> getShapes() {
        return sceneGraph.getShapes();
    }

    public void removeShapes() {
        sceneGraph.removeShapes();
    }
}
