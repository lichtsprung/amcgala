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

import org.amcgala.framework.animation.Animator;
import org.amcgala.framework.camera.AbstractCamera;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.event.WASDController;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.amcgala.framework.shape.Shape;
import com.google.common.eventbus.EventBus;
import org.amcgala.framework.scenegraph.visitor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Die Hauptklasse des Frameworks, die die Hauptaufgaben übernimmt. Sie
 * initialisiert die wichtigsten Datenstrukturen und ermöglicht ihren Zugriff.
 *
 * @author Robert Giacinto
 */
public abstract class Framework {

    private static final Logger log = LoggerFactory.getLogger(Framework.class);
    private SceneGraph scenegraph;
    private org.amcgala.framework.renderer.Renderer renderer;
    private Camera camera;
    private Animator animator;
    private List<Visitor> visitors;
    private RenderVisitor rv;
    private WASDController wasdController;
    private double aspectRatio;
    private double fieldOfView;
    private int screenWidth;
    private int screenHeight;
    private JFrame frame;
    private EventBus inputEventBus;
    private String id;

    /**
     * Erstellt ein neues Framework, das eine grafische Ausgabe in der Auflösung
     * width x height hat.
     *
     * @param width  die Breite der Auflösung
     * @param height die Höhe der Auflösung
     */
    public Framework(int width, int height) {
        log.info("Initialising framework");
        inputEventBus = new EventBus("Input");
        screenWidth = width;
        screenHeight = height;

        visitors = new ArrayList<Visitor>(10);
        scenegraph = new SceneGraph();
        aspectRatio = width / height;
        fieldOfView = Math.toRadians(76);

        frame = new JFrame("amCGAla Framework");
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                log.info("Shutting down...");
                shutdown();
            }
        });

        frame.setBackground(java.awt.Color.WHITE);
        frame.setVisible(true);


        camera = new SimplePerspectiveCamera(Vector3d.UNIT_Y, Vector3d.UNIT_Z, Vector3d.ZERO,2000);
        wasdController = new WASDController(camera);
        registerInputEventHandler(wasdController);

        renderer = new org.amcgala.framework.renderer.Renderer(width, height, frame);

        visitors.add(new InterpolationVisitor());

        animator = new Animator(60, 60);
        AnimationVisitor animationVisitor = new AnimationVisitor();
        visitors.add(animationVisitor);

        UpdateVisitor updateVisitor = new UpdateVisitor();
        visitors.add(updateVisitor);

        rv = new RenderVisitor();
        rv.setCamera(camera);
        rv.setRenderer(renderer);
        visitors.add(rv);


        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputEventBus.post(e);
            }
        });

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                inputEventBus.post(e);
            }
        });

        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                inputEventBus.post(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                inputEventBus.post(e);
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                inputEventBus.post(e);
            }
        });


        // TODO eigentlich unschön. Besser wäre es, würde man das Framework final deklarieren und man übergibt eine Szene, die vom Framework gerendet wird.
        initGraph();

    }

    /**
     * Jedes Programm, das auf das Framework zurückgreift implementiert diese
     * Methode. Hier findet die spezifische Initialisierung des Szenengraphs
     * statt. Hier können zum Beispiel Objekte an den Szenengraph gehängt
     * werden.
     */
    public abstract void initGraph();

    /**
     * Fügt der Szene ein neues zeichenbares Objekt hinzu. Dieses wird an den
     * Root-Knoten angehängt.
     *
     * @param shape das Grafikobjekt, das der Szene hinzugefügt wird
     */
    public void add(Shape shape) {
        scenegraph.addShape(shape);
    }
    
    /**
     * Fügt der Szene eine neue Lichtquelle hinzu. Diese wird an den Root-Knoten angehängt.
     * @param light das Lichtobjekt, das der Szene hinzugefügt wird.
     */
    public void add(Light light) {
    	scenegraph.addLight(light);
    }

    /**
     * Fügt dem Szenengraph einen neuen Knoten hinzu.
     *
     * @param node der neue Knoten
     */
    public void add(Node node) {
        scenegraph.addNode(node);
    }
    
    /**
     * Fügt einen Knoten an einen anderen bestimmten Knoten hinzu.
     * @param label der Name des Knotens
     * @param node der Knoten der angehängt wird
     */
    public void add(String label, Node node) {
    	scenegraph.addNode(node, label);
    }
    
    /**
     * Fügt dem benannten Knoten das Shapeobjekt hinzu.
     * @param label der name des Knotens
     * @param shape das Shapeobjekt
     */
    public void add(String label, Shape shape) {
    	scenegraph.addShape(label, shape);
    }

    /**
     * Ändert den Renderer des Frameworks. Damit ist es möglich die Ausgabe des
     * Frameworks zu verändern. Abhängig von der Implementierung des Renderers.
     *
     * @param renderer der neue Renderer
     */
    public void setRenderer(org.amcgala.framework.renderer.Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Ändert den Szenengraph, der vom Framework verwendet wird.
     *
     * @param scenegraph der neue Szenengraph
     */
    public void setScenegraph(SceneGraph scenegraph) {
        this.scenegraph = scenegraph;
    }

    /**
     * Das Seitenverhältnis der Ausgabe. Diese wird innerhalb der Kamera
     * benötigt.
     *
     * @return das Seitenverhältnis der Ausgabe
     */
    public double getAspectRatio() {
        return aspectRatio;
    }

    private void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * Gibt die Höhe der Ausgabe zurück.
     *
     * @return die Höhe in Pixeln
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Ändert die Höhe der Bildschirmausgabe.
     *
     * @param screenHeight die Höhe der Bildschirmausgabe in Pixeln
     */
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
        setAspectRatio(screenWidth / screenHeight);
    }

    /**
     * Gibt die Breite der Bildschirmausgabe in Pixeln zurück.
     *
     * @return die Breite der Bildschirmausgabe in Pixeln
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Ändert die Breite der Bildschirmausgabe.
     *
     * @param screenWidth die neue Breite der Bildschirmausgabe in Pixeln
     */
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
        setAspectRatio(screenWidth / screenHeight);
    }
    
    
    /**
     * Ändert die Hintergrundfarbe.
     */
    
    public void setBackgroundColor(java.awt.Color backgroundcolor) {
    	frame.setBackground(backgroundcolor);
    }
    
    /**
     * Gibt die aktuelle Hintergrundfarbe zurück.
     * @return
     */
    public java.awt.Color getBackgroundColor() {
    	return frame.getBackground();
    }

    /**
     * Gibt den Animator zurück.
     *
     * @return der Animator des Frameworks
     */
    public Animator getAnimator() {
        return animator;
    }

    /**
     * Gibt die Kamera zurück.
     *
     * @return die Kamera des Frameworks
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Gibt den Renderer zurück.
     *
     * @return der Renderer des Frameworks
     */
    public org.amcgala.framework.renderer.Renderer getRenderer() {
        return renderer;
    }

    /**
     * Gibt den Szenengraphen zurück.
     *
     * @return der Szenengraph des Frameworks
     */
    public SceneGraph getScenegraph() {
        return scenegraph;
    }

    /**
     * Gibt die Liste der registrierten Visitor zurück.
     *
     * @return die registrierten Visitor
     */
    public List<Visitor> getVisitors() {
        return Collections.unmodifiableList(visitors);
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

    /**
     * Beendet ein laufendes Framework. Falls es etwas aufzuräumen gibt, dann
     * passiert das hier.
     */
    public void shutdown() {
        System.exit(0);
    }

    /**
     * Fügt dem Framework einen neuen KeyAdapter hinzu, der KeyEvents abfängt
     * und behandelt.
     *
     * @param keyAdapter der KeyAdapter, der dem Framework hinzugefügt werden
     *                   soll
     * @deprecated wird zum Ende des Semesters entfernt, da auf den Eventbus
     *             zurückgegriffen wird.
     */
    public void addKeyAdapter(KeyAdapter keyAdapter) {
        frame.addKeyListener(keyAdapter);
    }

    /**
     * Entfernt einen KeyListener aus dem Framework.
     *
     * @param keyAdapter der KeyListener, der entfernt werden soll
     * @deprecated wird zum Ende des Semesters entfernt, da auf den Eventbus
     *             zurückgegriffen wird.
     */
    public void removeKeyAdapter(KeyAdapter keyAdapter) {
        frame.removeKeyListener(keyAdapter);
    }

    /**
     * Entfernt einen MouseAdapter aus dem Framework.
     *
     * @param mouseAdapter der MouseAdapter, der entfernt werden soll
     * @deprecated wird zum Ende des Semesters entfernt, da auf den Eventbus
     *             zurückgegriffen wird.
     */
    public void removeMouseAdapter(MouseAdapter mouseAdapter) {
        frame.removeMouseListener(mouseAdapter);
        frame.removeMouseMotionListener(mouseAdapter);
        frame.removeMouseWheelListener(mouseAdapter);
    }

    /**
     * Fügt dem Framework einen neuen MouseAdapter hinzu, der die MouseEvents
     * abfängt und behandelt.
     *
     * @param mouseAdapter der MouseAdapter, der dem Framework hinzugefügt
     *                     werden soll
     * @deprecated wird zum Ende des Semesters entfernt, da auf den Eventbus
     *             zurückgegriffen wird.
     */
    public void addMouseAdapter(MouseInputAdapter mouseAdapter) {
        frame.addMouseListener(mouseAdapter);
        frame.addMouseMotionListener(mouseAdapter);
        frame.addMouseWheelListener(mouseAdapter);
    }

    /**
     * Ändert die Kamera.
     *
     * @param camera die neue Kamera
     */
    public void setCamera(AbstractCamera camera) {
        this.camera = camera;
        rv.setCamera(camera);
        wasdController.setCamera(camera);
    }

    /**
     * Registriert einen neuen Eventhandler bei der EventQueue.
     *
     * @param handler der neue Inputhandler
     */
    public void registerInputEventHandler(InputHandler handler) {
        inputEventBus.register(handler);
    }

    /**
     * Entfernt einen Eventhandler aus der Liste der Subscriber.
     *
     * @param handler der Inputhandler, der entfernt werden soll
     */
    public void unregisterInputEventHandler(InputHandler handler) {
        inputEventBus.unregister(handler);
    }
}
