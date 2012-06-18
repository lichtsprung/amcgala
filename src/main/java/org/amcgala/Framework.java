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
package org.amcgala;

import com.google.common.eventbus.EventBus;
import org.amcgala.framework.Scene;
import org.amcgala.framework.animation.Animator;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.scenegraph.visitor.RenderVisitor;
import org.amcgala.framework.scenegraph.visitor.UpdateVisitor;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Die Hauptklasse des Frameworks, die die Hauptaufgaben übernimmt. Sie
 * initialisiert die wichtigsten Datenstrukturen und ermöglicht ihren Zugriff.
 *
 * @author Robert Giacinto
 */
public class Framework {

    private static final Logger log = LoggerFactory.getLogger(Framework.class);
    private SceneGraph scenegraph;
    private Renderer renderer;
    private Camera camera;
    private Animator animator;
    private List<Visitor> visitors;
    private JFrame frame;
    private EventBus eventBus;
    private Map<String, Scene> scenes;
    private Scene activeScene;
    private RenderVisitor rv;

    /**
     * Erstellt ein neues Framework, das eine grafische Ausgabe in der Auflösung
     * width x height hat.
     *
     * @param width  die Breite der Auflösung
     * @param height die Höhe der Auflösung
     */
    public Framework(int width, int height) {
        log.info("Initialising framework");
        eventBus = new EventBus("Input");

        visitors = new ArrayList<Visitor>(10);
        scenegraph = new DefaultSceneGraph();

        scenes = new HashMap<String, Scene>();


        frame = new JFrame("amCGAla Framework");
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setBackground(Color.WHITE);
        frame.setVisible(true);


        // TODO Zahlen weg und eine Konfigurationsdatei einführen!
        animator = new Animator(60, 60);

        visitors.add(new UpdateVisitor());

        rv = new RenderVisitor();
        rv.setCamera(camera);
        rv.setRenderer(renderer);
        visitors.add(rv);


        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                eventBus.post(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                eventBus.post(e);
            }
        });

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                eventBus.post(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                eventBus.post(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                eventBus.post(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                eventBus.post(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                eventBus.post(e);
            }
        });

        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                eventBus.post(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                eventBus.post(e);
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                eventBus.post(e);
            }
        });
    }


    /**
     * Aktualisiert den Szenengraphen, in dem die einzelnen, registrierten
     * Visitor den Szenengraphen besuchen.
     */
    public void update() {
        if (camera != null) {
            for (Visitor v : visitors) {
                scenegraph.accept(v);
            }
        }
    }

    /**
     * Rendert den Szenengraphen mithilfe des registrierten Renderers.
     */
    public void show() {
        if (renderer != null) {
            renderer.show();
        }
    }

    /**
     * Startet das Framework und aktualisiert den Szenengraphen mithilfe eines
     * Animators.
     */
    public void start() {
        if (animator == null) {
            update();
            show();
        } else {
            animator.setFramework(this);
            animator.start();
        }
    }

    /**
     * Pausiert die Aktualisierung des Frameworks.
     */
    public void pause() {
        if (animator != null) {
            animator.stop();
        }
    }


    public void addScene(Scene scene) {
        scenes.put(scene.getLabel(), scene);
        if (activeScene == null) {
            loadScene(scene);
        }
    }

    public void setActiveScene(Scene scene) {
        if (!scenes.values().contains(scene)) {
            scenes.put(scene.getLabel(), scene);
        }
        activeScene = scene;
    }

    public void loadScene(String label) {
        checkArgument(scenes.containsKey(label), "Es existiert keine Szene mit diesem Namen!");
        activeScene = scenes.get(label);
        loadScene(activeScene);
    }

    public void loadScene(Scene scene) {
        camera = scene.getCamera();
        renderer = scene.getRenderer();
        renderer.setFrame(frame);
        scenegraph = scene.getSceneGraph();
        rv.setCamera(camera);
        rv.setRenderer(renderer);
    }


    public void setFPS(int fps) {
        animator.setFramesPerSecond(fps);
    }
}
