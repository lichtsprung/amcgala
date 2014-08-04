package org.amcgala.renderer;

import org.amcgala.Framework;
import org.amcgala.event.*;
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
import java.awt.event.*;

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

        final Framework fr = framework;

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

            frame.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    } else {
                        if (fr.hasActiveScene()) {
                            fr.getActiveScene().getEventBus().post(new KeyPressedEvent(e));
                        }
                        fr.getEventBus().post(new KeyPressedEvent(e));
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(new KeyReleasedEvent(e));
                    }

                    fr.getEventBus().post(new KeyReleasedEvent(e));
                }
            });

            frame.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(new MouseClickedEvent(e));
                    }

                    fr.getEventBus().post(new MouseClickedEvent(e));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(new MousePressedEvent(e));
                    }

                    fr.getEventBus().post(new MousePressedEvent(e));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(new MouseReleasedEvent(e));
                    }

                    fr.getEventBus().post(new MouseReleasedEvent(e));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(e);
                    }

                    fr.getEventBus().post(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(e);
                    }

                    fr.getEventBus().post(e);
                }
            });

            frame.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(e);
                    }

                    fr.getEventBus().post(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(e);
                    }

                    fr.getEventBus().post(e);
                }
            });

            frame.addMouseWheelListener(new MouseWheelListener() {

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (fr.hasActiveScene()) {
                        fr.getActiveScene().getEventBus().post(e);
                    }

                    fr.getEventBus().post(e);
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
