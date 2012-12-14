package org.org.amcgala.examples.raytracer;

import org.amcgala.Framework;
import org.amcgala.Scene;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.raytracer.RGBColor;
import org.amcgala.framework.raytracer.material.MirrorMaterial;
import org.amcgala.framework.raytracer.material.TransparencyMaterial;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.shape3d.CheckerPlane;
import org.amcgala.framework.shape.shape3d.Sphere;

/**
 * Dieser Test sollte eine Kugel raytracen.
 */
public class RaySphereTest {

    public RaySphereTest() {
        Framework framework = Framework.createInstance(600, 600);
        Scene scene = new Scene("raytracer");
        Node sphereNode = new Node("sphere node");

        Sphere sphere = new Sphere(new Vector3d(110, 100, -280), 50);
        sphere.setMaterial(new MirrorMaterial(0.1f, new RGBColor(0.0f, 0.5f, 0.0f)));
        scene.add(sphere, sphereNode);


        Sphere sphere2 = new Sphere(new Vector3d(110, 100, -80), 50);

        sphere2.setMaterial( new TransparencyMaterial(0f, 0.4f, 1f, 1.5f, new RGBColor(1, 1, 0)));
        scene.add(sphere2, sphereNode);

        CheckerPlane plane = new CheckerPlane(new Vector3d(0, -1800, 0), new Vector3d(0, 1, 0),
                new RGBColor(0, 0, 0), new RGBColor(1, 1, 1), new RGBColor(1, 1, 0));

        scene.add(plane, sphereNode);

        framework.addScene(scene);
    }

    public static void main(String[] args) {
        new RaySphereTest();
    }
}
