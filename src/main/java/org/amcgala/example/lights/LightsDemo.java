package org.amcgala.example.lights;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.lighting.AmbientLight;
import org.amcgala.framework.lighting.PointLight;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.shape3d.Mesh;

import java.awt.Color;

/**
 * Beispiel, das den Umgang mit Lichtquellen zeigt.
 */
public class LightsDemo extends Amcgala{

    public LightsDemo() {
        Scene lightScene = new Scene("Light scene");
        PointLight pointLight = new PointLight("punktlicht", new AmbientLight("umgebungslicht", 0.5, new Color(255, 255, 255)), new Vector3d(150, 100, 1));

        Mesh m = new Mesh(new Vector3d(-50, -50, 500), 100, 100, 100);

        lightScene.addShape(m);
        lightScene.addLight(pointLight);

        framework.addScene(lightScene);
        framework.start();
    }

    public static void main(String[] args) {
        new LightsDemo();
    }
}
