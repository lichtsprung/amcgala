package org.amcgala.example.plyparser;

import org.amcgala.Framework;
import org.amcgala.framework.camera.SimplePerspectiveCamera;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.Polygon;
import org.amcgala.framework.shape.util.PLYPolygonParser;

import java.awt.event.MouseEvent;
import java.io.InputStream;

import com.google.common.eventbus.Subscribe;

/**
 * Beispiel, das die Verwendung des PLYParsers demonstriert.
 *
 * @author Steffen Tröster
 */
public class PLYExampleMain extends Framework implements InputHandler {

    private static final int DESTINATION = 1900;
    private Vector3d direction = new Vector3d(0.3, 0.5, 0.2);
    private Vector3d up = Vector3d.UNIT_Y;
    private Vector3d position = new Vector3d(0, 0, 0);

    public PLYExampleMain(int width, int height) {
        super(width, height);
        setCamera(new SimplePerspectiveCamera(up, position, direction,
                DESTINATION));
        registerInputEventHandler(this);
    }

    @Override
    public void initGraph() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("amcgala/example/plyparser/monkey.ply");
        try {
            for (Polygon p : PLYPolygonParser.parseAsPolygonList(inputStream,
                    150)) {
                add(p);
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Polygone! Bitte ueberpruefen Sie die Exporteigenschaften!");
        }

    }

    @Subscribe
    public void handleMouse(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_MOVED) {
            direction.x = (double) e.getX() / (double) getScreenWidth();
            direction.y = (double) e.getY() / (double) getScreenHeight();
            getCamera().setDirection(direction);
            getCamera().setPosition(position);
            getCamera().setVup(up);
        }
    }

    public static void main(String[] args) {
        PLYExampleMain m = new PLYExampleMain(500, 500);
        m.start();
    }
}
