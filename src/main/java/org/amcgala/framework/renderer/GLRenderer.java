package org.amcgala.framework.renderer;

import org.amcgala.Framework;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.shape.primitives.LinePrimitive;
import org.amcgala.framework.shape.primitives.PointPrimitive;
import org.amcgala.framework.shape.primitives.TrianglePrimitive;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.lwjgl.opengl.GL11.*;


/**
 * Die OpenGL Version des Renderers. Es wird LWJGL als GL Anbindung verwendet.
 */
public class GLRenderer implements Renderer {
    private static final Logger log = LoggerFactory.getLogger(GLRenderer.class);
    private Color currentColor;
    private Framework framework;

    public GLRenderer() {
        log.info("Creating new instance of GLRenderer...");
        this.framework = Framework.getInstance();
        try {
            Frame frame = new Frame("GL Renderer");
            frame.setSize(framework.getWidth(), framework.getHeight());

            Canvas canvas = new Canvas();
            frame.setBackground(Color.BLACK);
            frame.add(canvas);
            frame.setVisible(true);

//            TODO Besser wäre Display.destroy aufzurufen.
//            Display.destroy muss vom selben Thread wie der OpenGL Context aufgerufen werden. Wir befinden uns hier
//            aber im AWT Event Thread und bekommen bei Display.destroy eine Exception geworfen. Könnte umgegangen werden,
//            wenn dem Renderer von außen eine Nachricht geschickt wird, sich selbst zu beenden.
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            Display.setParent(canvas);
            Display.setVSyncEnabled(true);
            Display.setInitialBackground(1f, 1f, 1f);
            PixelFormat pf = new PixelFormat().withSamples(1);
            Display.create(pf);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, framework.getWidth(), framework.getHeight(), 0, 1, -1);
            glMatrixMode(GL_MODELVIEW);
        } catch (LWJGLException e) {
            log.error("OpenGL konnte nicht initialisiert werden: {}", e);
        }
    }


    @Override
    public int getWidth() {
        return Display.getWidth();
    }

    @Override
    public int getHeight() {
        return Display.getHeight();
    }


    @Override
    public void setColor(Color color) {
        float[] colorComp = color.getColorComponents(null);
        glColor3f(colorComp[0], colorComp[1], colorComp[2]);
        currentColor = color;
    }


    @Override
    public void show() {
        DisplayList list = framework.getCurrentState();
        glBegin(GL_LINES);
        for (LinePrimitive line : list.lines) {
            setColor(line.color);
            for (Vertex3f v : line.vertices) {
                glVertex3f(v.x, v.y, v.z);
            }
        }
        glEnd();

        glBegin(GL_TRIANGLES);
        for (TrianglePrimitive triangle : list.triangles) {
            setColor(triangle.color);
            for (Vertex3f v : triangle.vertices) {
                glVertex3f(v.x, v.y, v.z);
            }
        }
        glEnd();

        glBegin(GL_POINTS);
        for (PointPrimitive point : list.points) {
            setColor(point.color);
            Vertex3f v = point.vertices.get(0);
            glVertex3f(v.x, v.y, v.z);
        }
        glEnd();

        Display.update();
    }


    @Override
    public Color getColor() {
        return currentColor;
    }
}
