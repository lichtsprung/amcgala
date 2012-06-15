package org.amcgala.framework;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.shape.Shape;

/**
 * Ein {@code Scene} Objekt verwaltet alle Objekte und den dazugehörigen {@link org.amcgala.framework.scenegraph.SceneGraph},
 * der über die Klasse {@link org.amcgala.Framework} dargestellt werden kann.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Scene {
    public static final Scene EMPTY_SCENE = new Scene();
    private SceneGraph sceneGraph;
    private Camera camera;

    public Scene(){
        sceneGraph = new SceneGraph();
        camera = new SimplePerspectiveCamera(Vector3d.UNIT_Y, Vector3d.UNIT_Z, Vector3d.ZERO, 2000);
    }

    public void add(Shape shape){
        sceneGraph.add(shape);
    }

    public void add(Shape shape, String label){
        sceneGraph.add(shape, label);
    }

    public void add(Node node){
        sceneGraph.add(node);
    }

    public void add(Node node, String label) {
        sceneGraph.add(node, label);
    }

    public Shape getShape(String label) {
        return sceneGraph.getShape(label);
    }
}
