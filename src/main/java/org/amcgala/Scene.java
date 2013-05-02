package org.amcgala;

import com.google.common.eventbus.EventBus;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.scenegraph.transform.Transformation;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Ein {@link Scene} Objekt verwaltet alle Objekte und den dazugehörigen {@link org.amcgala.framework.scenegraph.DefaultSceneGraph},
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
     * Fügt das AbstractShape dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} hinzu.
     *
     * @param shape das hinzuzufügende Objekt
     */
    public void addShape(Shape shape) {
        sceneGraph.addShape(shape);
    }

    /**
     * Fügt dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} ein {@link org.amcgala.framework.scenegraph.Node} hinzu.
     *
     * @param node der neue Knoten
     */
    public void addNode(Node node) {
        sceneGraph.addNode(node);
    }

    /**
     * Fügt der Szene ein neues Shapeobjekt hinzu. Dieses wird dem Szenengraph an dem übergebenen Knoten angehängt.
     * TODO das ist verwirrend, dass man erst den Knoten der Szene hinzufügen muss, um ein Shape dranhängen zu können.
     *
     * @param shape das Shape, das der Szene hinzugefügt werden soll
     * @param node  der Knoten, an dem das Shape angehängt werden soll
     */
    public void add(Shape shape, Node node) {
        sceneGraph.addShape(shape, node);
    }


    /**
     * Fügt der Szene ein neues Shapeobjekt hinzu. Es wird dem Knoten mit dem übergebenen Label angehängt.
     *
     * @param shape     das Shape, das der Szene hinzugefügt werden soll
     * @param nodeLabel das Label des Knotens
     */
    public void addShape(Shape shape, String nodeLabel) {
        sceneGraph.addShape(shape, nodeLabel);
    }

    /**
     * Fügt der Szene einen neuen Knoten hinzu und hängt diesen an den Elternknoten mit dem übergebenen Label.
     *
     * @param node        der neue Knoten, der hinzugefügt werden soll
     * @param parentLabel das Label des Elternknotens
     */
    public void addNode(Node node, String parentLabel) {
        sceneGraph.addNode(node, parentLabel);
    }

    /**
     * Fügt einem Elternknoten einen neuen Kindsknoten im Szenengraph hinzu.
     * TODO das Erweitern der Baumhierarchie über eine Methode in einer Szene ist umständlich. Die Hierarchie sollte automatisch aktualisiert werden, wenn ein neuer Knoten hinzufügt wird.
     *
     * @param child  der neue Kindsknoten
     * @param parent der Elternknoten
     */
    public void addNode(Node child, Node parent) {
        sceneGraph.addNode(child, parent);
    }

    /**
     * Fügt dem root {@link org.amcgala.framework.scenegraph.Node} des {@link org.amcgala.framework.scenegraph.SceneGraph} eine Transformation hinzu,
     *
     * @param transformation die Transformation, die hinzugefügt werden soll
     */
    public void addTransformation(Transformation transformation) {
        sceneGraph.addTransformation(transformation);
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
     * Ändert die von der Szene verwendete {@link org.amcgala.framework.camera.Camera}.
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
    public void addInputHandler(InputHandler inputHandler, String label) {
        inputHandlers.put(label, inputHandler);
        eventBus.register(inputHandler);
    }

    /**
     * Entfernt einen Eventhandler aus der Liste der Subscriber.
     *
     * @param label Name des {@code InputHandler} der entfernt werden soll
     */
    public void removeInputHandler(String label) {
        checkArgument(inputHandlers.containsKey(label), "InputHandler mit Namen " + label + " konnte nicht gefunden werden");
        eventBus.unregister(inputHandlers.get(label));
    }

    /**
     * Entfernt ein Shape aus der Szene.
     *
     * @param label das Label des Shapes, das entfernt werden soll
     */
    public void removeShape(String label) {
        sceneGraph.removeShape(label);
    }

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

    public Collection<Shape> getShapes() {
        return sceneGraph.getAllShapes();
    }
}
