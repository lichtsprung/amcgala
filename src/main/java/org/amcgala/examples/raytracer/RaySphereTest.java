package org.amcgala.examples.raytracer;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.shape3d.Sphere;

/**
 * Dieser Test sollte eine Kugel raytracen.
 */
public class RaySphereTest extends Amcgala {
    public RaySphereTest() {
        Scene scene = new Scene("raytracer");
        Node sphereNode = new Node("sphere node");
        Sphere sphere = new Sphere(new Vector3d(0, 0, -100), 20);
        scene.add(sphere, sphereNode);
        framework.addScene(scene);
        framework.start();
    }

    public static void main(String[] args) {
        new RaySphereTest();
    }
}
