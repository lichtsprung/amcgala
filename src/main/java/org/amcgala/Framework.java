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
import org.amcgala.framework.animation.Animator;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.event.KeyPressedEvent;
import org.amcgala.framework.event.KeyReleasedEvent;
import org.amcgala.framework.event.MouseClickedEvent;
import org.amcgala.framework.event.MousePressedEvent;
import org.amcgala.framework.event.MouseReleasedEvent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Die Hauptklasse des Frameworks, die die Hauptaufgaben übernimmt. Sie
 * initialisiert die wichtigsten Datenstrukturen und ermöglicht ihren Zugriff.
 *
 * @author Robert Giacinto
 * @version 2.0
 */
public final class Framework {

    private static final Logger log = LoggerFactory.getLogger(Framework.class);
    private static Framework instance;

    private SceneGraph scenegraph;
    private Renderer renderer;
    private Camera camera;
    private Animator animator;
    private List<Visitor> visitors;
    private JFrame frame;
    private EventBus sceneEventBus;
    private EventBus frameworkEventBus;
    private Map<String, Scene> scenes;
    private Scene activeScene;
    private RenderVisitor renderVisitor;
    private UpdateVisitor updateVisitor;
    private Map<String, InputHandler> frameworkInputHandlers;
    private List<Scene> sceneList;
    private int currentSceneIndex;
    private boolean paused;

    private int width;
    private int height;

    public static Framework createInstance(int width, int height) {
        checkArgument(instance == null, "Es können keine weiteren Instanzen von Framework erzeugt werden!");
        instance = new Framework(width, height);
        return instance;
    }

    public static Framework getInstance(){
        if(instance == null) {
            return createInstance(800, 600);
        }else{
            return instance;
        }
    }


    /**
     * Erstellt ein neues Framework, das eine grafische Ausgabe in der Auflösung
     * width x height hat.
     *
     * @param width  die Breite der Auflösung
     * @param height die Höhe der Auflösung
     */
    private Framework(int width, int height) {
        log.info("Initialising framework");
        this.width = width;
        this.height = height;

        frameworkInputHandlers = new HashMap<String, InputHandler>();
        frameworkEventBus = new EventBus("Framework Input Event Bus");

        sceneEventBus = new EventBus();

        visitors = new ArrayList<Visitor>(10);
        scenegraph = new DefaultSceneGraph();

        scenes = new HashMap<String, Scene>();
        sceneList = new ArrayList<Scene>();
        currentSceneIndex = 0;


        frame = new JFrame("amCGAla Framework");
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setBackground(Color.WHITE);
        frame.setVisible(true);


        // TODO Zahlen weg und eine Konfigurationsdatei einführen!
        animator = new Animator(60, 60);

        updateVisitor = new UpdateVisitor();
        visitors.add(updateVisitor);

        renderVisitor = new RenderVisitor();
        visitors.add(renderVisitor);


        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                sceneEventBus.post(new KeyPressedEvent(e));
                frameworkEventBus.post(new KeyPressedEvent(e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                sceneEventBus.post(new KeyReleasedEvent(e));
                frameworkEventBus.post(new KeyReleasedEvent(e));
            }
        });

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                sceneEventBus.post(new MouseClickedEvent(e));
                frameworkEventBus.post(new MouseClickedEvent(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                sceneEventBus.post(new MousePressedEvent(e));
                frameworkEventBus.post(new MousePressedEvent(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                sceneEventBus.post(new MouseReleasedEvent(e));
                frameworkEventBus.post(new MouseReleasedEvent(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sceneEventBus.post(e);
                frameworkEventBus.post(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sceneEventBus.post(e);
                frameworkEventBus.post(e);
            }
        });

        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                sceneEventBus.post(e);
                frameworkEventBus.post(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                sceneEventBus.post(e);
                frameworkEventBus.post(e);
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                sceneEventBus.post(e);
                frameworkEventBus.post(e);
            }
        });
    }


    /**
     * Aktualisiert den Szenengraphen, in dem die einzelnen, registrierten
     * Visitor den Szenengraphen besuchen.
     */
    public void update() {
        if (camera != null && !paused) {
            for (Visitor v : visitors) {
                scenegraph.accept(v);
            }
        }
    }

    /**
     * Rendert den Szenengraphen mithilfe des registrierten Renderers.
     */
    public void show() {
        if (renderer != null && !paused) {
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
     * Fügt dem Framework eine neue {@link Scene} hinzu.
     *
     * @param scene die neue Szene
     */
    public void addScene(Scene scene) {
        checkArgument(!scenes.containsKey(scene.getLabel()), "Es existiert bereits eine Szene mit dem gleichen Namen!");
        scenes.put(scene.getLabel(), scene);
        sceneList.add(scene);

        if (activeScene == null) {
            loadScene(scene);
        }
    }

    /**
     * Setzt eine {@link Scene} als aktive Szene innerhalb des Frameworks.
     * Existiert die übergebene Szene noch nicht in der Menge aller Szenen innerhalb des Frameworks, wird die
     * Szene unter dem Label der Szene im Framework gespeichert bevor sie geladen wird.
     *
     * @param scene die Szene, die geladen werden soll
     */
    public void setActiveScene(Scene scene) {
        if (!scenes.containsKey(scene.getLabel())) {
            scenes.put(scene.getLabel(), scene);
            sceneList.add(scene);
        }
        activeScene = scene;
    }

    /**
     * Lädt eine bestimmte, schon im Framework gespeicherte Szene über das Label dieser Szene.
     *
     * @param label das Label der Szene, die geladen werden soll
     */
    public void loadScene(String label) {
        checkArgument(scenes.containsKey(label), "Es existiert keine Szene mit diesem Namen!");
        Scene scene = scenes.get(label);
        loadScene(scene);
    }

    /**
     * Das Framework lädt die Szene, indem {@link Camera}, {@link Renderer} und {@link SceneGraph} aus
     * der Szene geladen werden.
     *
     * @param scene Szene, die geladen werden soll
     */
    public void loadScene(Scene scene) {
        log.info("loading scene: " + scene.getLabel());
        paused = true;
        updateVisitor.setPaused(paused);

        camera = scene.getCamera();
        camera.setWidth(frame.getWidth());
        camera.setHeight(frame.getHeight());

        renderer = scene.getRenderer();
        renderer.setFrame(frame);

        scenegraph = scene.getSceneGraph();
        renderVisitor.setRenderer(renderer);
        renderVisitor.setCamera(camera);
        sceneEventBus = scene.getEventBus();
        activeScene = scene;
        paused = false;
        updateVisitor.setPaused(paused);
    }

    /**
     * Gibt die Referenz auf eine Szene zurück.
     *
     * @param label das Label der Szene
     *
     * @return die Szene mit dem übergebenen Label
     *
     * @throws IllegalArgumentException wenn keine Szene mit dem übergebenen Label existiert
     */
    public Scene getScene(String label) {
        checkArgument(scenes.containsKey(label), "Es existiert keine Szene mit diesem Namen!");
        return scenes.get(label);
    }


    /**
     * Ändert die Anzahl der Frames, die pro Sekunden berechnet werden sollen.
     *
     * @param fps die Frames pro Sekunde
     */
    public void setFPS(int fps) {
        animator.setFramesPerSecond(fps);
    }

    /**
     * Entfernt eine Szene aus dem Framework.
     *
     * @param scene die Referenz der Szene, die entfernt werden soll
     */
    public void removeScene(Scene scene) {
        checkArgument(scenes.containsValue(scene), "Szene " + scene.getLabel() + " konnte nicht gefunden werden");
        checkArgument(!activeScene.equals(scene), "Aktive Szene kann nicht entfernt werden!");
        scenes.remove(scene.getLabel());
    }

    /**
     * Entfernt eine Szene aus dem Framework.
     *
     * @param label das Label der Szene, die entfernt werden soll
     */
    public void removeScene(String label) {
        checkArgument(scenes.containsKey(label), "Szene " + label + " konnte nicht gefunden werden");
        checkArgument(!activeScene.getLabel().equalsIgnoreCase(label), "Aktive Szene kann nicht entfernt werden!");
        scenes.remove(label);
    }

    /**
     * Fügt dem Framework einen neuen {@link InputHandler} hinzu.
     *
     * @param inputHandler der neue InputHandler
     * @param label        der Bezeichner dieses InputHandlers
     */
    public void addInputHandler(InputHandler inputHandler, String label) {
        frameworkEventBus.register(inputHandler);
        frameworkInputHandlers.put(label, inputHandler);
    }


    /**
     * Entfernt einen {@link InputHandler} aus dem Framework.
     *
     * @param label der Bezeichner des InputHandlers, der entfernt werden soll
     */
    public void removeInputHandler(String label) {
        checkArgument(frameworkInputHandlers.containsKey(label), "InputHandler mit Label " + label + " konnte nicht gefunden werden.");
        frameworkEventBus.unregister(frameworkInputHandlers.get(label));
        frameworkInputHandlers.remove(label);
    }

    /**
     * Wechselt zur nächsten Szene im Framework.
     */
    public void nextScene() {
        if (sceneList.size() > 1) {
            if (currentSceneIndex + 1 < sceneList.size()) {
                currentSceneIndex++;
                loadScene(sceneList.get(currentSceneIndex));
            } else {
                currentSceneIndex = 0;
                loadScene(sceneList.get(currentSceneIndex));
            }
        }
    }

    /**
     * Gibt die gerade aktive Szene zurück.
     * @return die aktive Szene
     */
    public Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Gibt die Anzahl der Szenen zurück, die vom Framework verwaltet werden.
     *
     * @return die Anzahl der Szenen im Framework
     */
    public int getSceneCount() {
        return sceneList.size();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
