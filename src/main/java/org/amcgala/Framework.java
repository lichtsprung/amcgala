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
import org.amcgala.framework.raytracer.Raytracer;
import org.amcgala.framework.renderer.DefaultRenderer;
import org.amcgala.framework.renderer.DisplayList;
import org.amcgala.framework.renderer.GLRenderer;
import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.scenegraph.visitor.UpdateVisitor;
import org.amcgala.framework.scenegraph.visitor.Visitor;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Die Hauptklasse des Frameworks, die die Hauptaufgaben übernimmt. Sie
 * initialisiert die wichtigsten Datenstrukturen und ermöglicht ihren Zugriff.
 * Folgende wichtige Funktionen werden vom Framework übernommen:
 * <ul>
 * <li>Laden von Szenen</li>
 * <li>Aktualisierung und Darstellung der aktiven Szene</li>
 * <li>Verwalten der InputHandler</li>
 * </ul>
 *
 * @author Robert Giacinto
 * @version 2.0
 */
public final class Framework {
    private static final Logger log = LoggerFactory.getLogger(Framework.class);
    public static final Properties properties = loadProperties();
    private static Framework instance;
    private SceneGraph scenegraph;
    private Animator animator;
    private Camera camera;
    private List<Visitor> visitors;
    private EventBus sceneEventBus;
    private EventBus frameworkEventBus;
    private Map<String, Scene> scenes;
    private Scene activeScene;
    private UpdateVisitor updateVisitor;
    private Raytracer raytracer;
    private Map<String, InputHandler> frameworkInputHandlers;
    private boolean paused;
    private int width;
    private int height;
    private boolean tracing;
    private boolean running;
    private DisplayList displayList;

    /**
     * Erstellt ein neues Framework, das eine grafische Ausgabe in der Auflösung
     * width x height hat.
     *
     * @param width  die Breite der Auflösung
     * @param height die Höhe der Auflösung
     */
    private Framework(int width, int height, FrameworkMode mode) {
        log.info("Initialising framework");
        this.width = width;
        this.height = height;


        frameworkInputHandlers = new HashMap<String, InputHandler>();
        frameworkEventBus = new EventBus("Framework Input Event Bus");

        sceneEventBus = new EventBus();

        visitors = new ArrayList<Visitor>(10);
        scenegraph = new DefaultSceneGraph();

        scenes = new HashMap<String, Scene>();

        updateVisitor = new UpdateVisitor();
        visitors.add(updateVisitor);

        //
        // FIXME Raytracer ist eigentlich unabhängig vom Framework. Vielleicht besser: Raytracer(Framework, RaytracerMode.(Window, File)).
        //
        //raytracer = new Raytracer();
        //

        switch (mode) {
            case SOFTWARE:
                animator = new Animator(60, 60, this, DefaultRenderer.class);
                break;
            case GL:
                animator = new Animator(60, 60, this, GLRenderer.class);
                break;
            case RAYTRACER:
                tracing = true;
                break;
        }
    }

    /**
     * Erzeugt eine neue Instanz des Frameworks. Die Größe des Fensters kann über die Parameter width und height
     * bestimmt werden.
     *
     * @param width  die Breite des Fensters
     * @param height die Höhe des Fensters
     * @return Referenz auf die Frameworksinstanz
     */
    public static Framework createInstance(int width, int height, FrameworkMode mode) {
        checkArgument(instance == null, "Es können keine weiteren Instanzen von Framework erzeugt werden!");
        instance = new Framework(width, height, mode);

        return instance;
    }

    /**
     * Erzeugt eine neue Instanz des Frameworks. Die Größe des Fensters kann über die Parameter width und height
     * bestimmt werden.
     * TODO Die sollte wieder weg oder private sein. Kann mich nicht mehr daran erinnern, wieso zwischen get und create unterschieden wird.
     *
     * @param width  die Breite des Fensters
     * @param height die Höhe des Fensters
     * @return Referenz auf die Frameworksinstanz
     */
    public static Framework createInstance(int width, int height) {
        return createInstance(width, height, FrameworkMode.SOFTWARE);
    }

    /**
     * Gibt die bereits erzeugte Instanz des Frameworks zurück. Wurde noch keine erstellt, wird eine der Standardgröße
     * 800x600 erstellt und zurückgegeben.
     * Es wird der {@link DefaultRenderer} verwendet.
     *
     * @return Referenz auf die Frameworksinstanz
     */
    public static Framework getInstance() {
        if (instance == null) {
            instance = createInstance(Integer.parseInt(properties.getProperty("amcgala.width")), Integer.parseInt(properties.getProperty("amcgala.height")), FrameworkMode.SOFTWARE);
            return instance;
        } else {
            return instance;
        }
    }

    /**
     * Gibt die bereits erzeugte Instanz des Frameworks zurück. Wurde noch keine erstellt, wird eine der Standardgröße
     * 800x600 erstellt und zurückgegeben.
     *
     * @return Referenz auf die Frameworksinstanz
     */
    public static Framework getInstance(FrameworkMode mode) {
        if (instance == null) {
            instance = createInstance(Integer.parseInt(properties.getProperty("amcgala.width")), Integer.parseInt(properties.getProperty("amcgala.height")), mode);
            return instance;
        } else {
            return instance;
        }
    }

    private static Properties loadProperties() {
        final Properties props = new Properties();
        log.info("Loading properties");
        final InputStream in = Framework.class.getResourceAsStream("amcgala.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            log.error("Couldn't load amcgala properties file.");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("Coulnd't close InputStream.");
            }
        }
        return props;  //To change body of created methods use File | Settings | File Templates.
    }

    public EventBus getEventBus() {
        return frameworkEventBus;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Properties getProperties() {
        return properties;
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
        if (tracing) {
            raytracer.traceScene();
        }
        Collection<Shape> shapes = scenegraph.getAllShapes();
        displayList = new DisplayList();
        for (Shape s : shapes) {
            displayList.add(s.getDisplayList());
        }
    }


    /**
     * Pausiert die Aktualisierung des Frameworks.
     */
    public void pause() {
        running = false;
    }

    public void stop() {
        animator.setRunning(false);
    }

    /**
     * Fügt dem Framework eine neue {@link Scene} hinzu.
     *
     * @param scene die neue Szene
     */
    public void addScene(Scene scene) {
        log.info("Adding scene {}", scene);
        checkArgument(!scenes.containsKey(scene.getLabel()), "Es existiert bereits eine Szene mit dem gleichen Namen!");
        scenes.put(scene.getLabel(), scene);

        if (activeScene == null) {
            loadScene(scene);
        }
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
     * Das Framework lädt die Szene, indem {@link org.amcgala.framework.camera.Camera}, {@link org.amcgala.framework.renderer.Renderer} und {@link org.amcgala.framework.scenegraph.SceneGraph} aus
     * der Szene geladen werden.
     *
     * @param scene Szene, die geladen werden soll
     */
    public void loadScene(Scene scene) {
        log.info("loading scene: " + scene.getLabel());
        paused = true;
        updateVisitor.setPaused(paused);

        if (scene.getCamera() != null) {
            camera = scene.getCamera();
            camera.setWidth(width);
            camera.setHeight(height);
        }

        scenegraph = scene.getSceneGraph();

        // TODO laden der Scene in Raytracer

        sceneEventBus = scene.getEventBus();
        activeScene = scene;
        paused = false;
        updateVisitor.setPaused(paused);
    }

    /**
     * Gibt die Referenz auf eine Szene zurück.
     *
     * @param label das Label der Szene
     * @return die Szene mit dem übergebenen Label
     * @throws IllegalArgumentException wenn keine Szene mit dem übergebenen Label existiert
     */
    public Scene getScene(String label) {
        checkArgument(scenes.containsKey(label), "Es existiert keine Szene mit diesem Namen!");
        return scenes.get(label);
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
     * Fügt dem Framework einen neuen {@link org.amcgala.framework.event.InputHandler} hinzu.
     *
     * @param inputHandler der neue InputHandler
     * @param label        der Bezeichner dieses InputHandlers
     */
    public void addInputHandler(InputHandler inputHandler, String label) {
        frameworkEventBus.register(inputHandler);
        frameworkInputHandlers.put(label, inputHandler);
    }

    /**
     * Entfernt einen {@link org.amcgala.framework.event.InputHandler} aus dem Framework.
     *
     * @param label der Bezeichner des InputHandlers, der entfernt werden soll
     */
    public void removeInputHandler(String label) {
        checkArgument(frameworkInputHandlers.containsKey(label), "InputHandler mit Label " + label + " konnte nicht gefunden werden.");
        frameworkEventBus.unregister(frameworkInputHandlers.get(label));
        frameworkInputHandlers.remove(label);
    }

    /**
     * Gibt die gerade aktive Szene zurück.
     *
     * @return die aktive Szene
     */
    public Scene getActiveScene() {
        return activeScene;
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
        }
        activeScene = scene;
    }

    /**
     * Gibt die Anzahl der Szenen zurück, die vom Framework verwaltet werden.
     *
     * @return die Anzahl der Szenen im Framework
     */
    public int getSceneCount() {
        return scenes.size();
    }

    /**
     * Gibt die Breite des Fensters zurück.
     *
     * @return die Breite des Fensters
     */
    public int getWidth() {
        return (width == 0) ? Integer.parseInt(properties.getProperty("amcgala.width")) : width;
    }

    /**
     * Gibt die Höhe des Fensters zurück.
     *
     * @return die Höhe des Fensters
     */
    public int getHeight() {
        return (height == 0) ? Integer.parseInt(properties.getProperty("amcgala.height")) : height;
    }

    public DisplayList getCurrentState() {
        return displayList;
    }
}
