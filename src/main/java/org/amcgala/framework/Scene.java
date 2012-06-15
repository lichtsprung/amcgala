package org.amcgala.framework;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.shape.Shape;

/**
 * Ein {@code Scene} Objekt verwaltet alle Objekte und den dazugehörigen {@link org.amcgala.framework.scenegraph.DefaultSceneGraph},
 * der über die Klasse {@link org.amcgala.Framework} dargestellt werden kann.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Scene {
    public static final Scene EMPTY_SCENE = new Scene();
    private SceneGraph sceneGraph;
    private Camera camera;
    private Renderer renderer;

    public Scene() {
        sceneGraph = new DefaultSceneGraph();
        camera = new SimplePerspectiveCamera(Vector3d.UNIT_Y, Vector3d.UNIT_Z, Vector3d.ZERO, 2000);
    }

    /**
     * Fügt das Shape dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} hinzu.
     * @param shape das hinzuzufügende Objekt
     */
    public void add(Shape shape) {
        sceneGraph.add(shape);
    }


     /**
     * Fügt dem Rootknoten des {@link org.amcgala.framework.scenegraph.DefaultSceneGraph} ein {@link Node} hinzu.
     * @param node der neue Knoten
     */
    public void add(Node node) {
        sceneGraph.add(node);
    }

    public void add(Shape shape, Node node){
        sceneGraph.add(shape, node);
    }

}
