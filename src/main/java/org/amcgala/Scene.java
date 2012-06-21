package org.amcgala;

import com.google.common.eventbus.EventBus;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.DefaultRenderer;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.shape.Shape;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Ein {@code Scene} Objekt verwaltet alle Objekte und den dazugehörigen {@link org.amcgala.framework.scenegraph.DefaultSceneGraph},
 * der über die Klasse {@link org.amcgala.Framework} dargestellt werden kann.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Scene {
    public static final Scene EMPTY_SCENE = new Scene("Empty Scene");
    private SceneGraph sceneGraph;
    private Camera camera;
    private Renderer renderer;
    private EventBus eventBus;
    private String label;
    private Map<String, InputHandler> inputHandlers;

    public Scene(String label) {
        this.label = label;
        sceneGraph = new DefaultSceneGraph();
        camera = new SimplePerspectiveCamera(Vector3d.UNIT_Y, Vector3d.UNIT_Z, Vector3d.ZERO, 2000);
        renderer = new DefaultRenderer(camera);
        eventBus = new EventBus();
        inputHandlers = new HashMap<String, InputHandler>();
    }

    /**
     * Fügt das AbstractShape dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} hinzu.
     *
     * @param shape das hinzuzufügende Objekt
     */
    public void add(Shape shape) {
        sceneGraph.add(shape);
    }

    /**
     * Fügt dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} ein {@link Node} hinzu.
     *
     * @param node der neue Knoten
     */
    public void add(Node node) {
        sceneGraph.add(node);
    }

    public void add(Shape shape, Node node) {
        sceneGraph.add(shape, node);
    }

    public Camera getCamera() {
        return camera;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

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
}
