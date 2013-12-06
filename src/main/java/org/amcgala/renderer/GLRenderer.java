package org.amcgala.renderer;

import org.amcgala.Framework;
import org.amcgala.math.Vertex3f;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.PointPrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;
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

    public GLRenderer(Framework framework) {
        log.info("Creating new instance of GLRenderer...");
        this.framework = framework;

        try {
            Frame frame = new Frame("amCGAla GL");
            frame.setSize(framework.getWidth(), framework.getHeight());

            Canvas canvas = new Canvas();
            frame.setBackground(Color.BLACK);
            frame.add(canvas);
            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            Display.setParent(canvas);
            Display.setVSyncEnabled(true);
            Display.setInitialBackground(0.95f, 0.95f, 0.95f);
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


    private void setColor(Color color) {
        float[] colorComp = color.getColorComponents(null);
        glColor3f(colorComp[0], colorComp[1], colorComp[2]);
        currentColor = color;
    }


    @Override
    public void render() {
        DisplayList list = framework.getCurrentState();
        glBegin(GL_LINES);
        for (LinePrimitive line : list.lines) {
            setColor(line.color.toAWTColor());
            glVertex3f(line.v0.x, line.v0.y, line.v0.z);
            glVertex3f(line.v1.x, line.v1.y, line.v1.z);
        }
        glEnd();

        glBegin(GL_TRIANGLES);
        for (TrianglePrimitive triangle : list.triangles) {
            setColor(triangle.color.toAWTColor());
            glVertex3f(triangle.v0.x, triangle.v0.y, triangle.v0.z);
            glVertex3f(triangle.v1.x, triangle.v1.y, triangle.v1.z);
            glVertex3f(triangle.v2.x, triangle.v2.y, triangle.v2.z);
        }
        glEnd();

        glBegin(GL_POINTS);
        for (PointPrimitive point : list.points) {
            setColor(point.color.toAWTColor());
            Vertex3f v = point.point;
            glVertex3f(v.x, v.y, v.z);
        }
        glEnd();

        // TODO Quads wieder hinzufuegen.

        Display.update();
    }

}
