package org.amcgala.framework.renderer;

import org.amcgala.Framework;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.shape.primitives.LinePrimitive;
import org.amcgala.framework.shape.primitives.TrianglePrimitive;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;


/**
 * Renderer, der die grafische Ausgabe über OpenGL beschleunigt.
 */
public class GLRenderer implements Renderer {
    private static final Logger log = LoggerFactory.getLogger(GLRenderer.class);
    private Camera camera;
    private Matrix transformationMatrix;
    private Color currentColor;
    private Framework framework;

    public GLRenderer(int width, int height, Framework framework) {
        log.info("Creating new instance of GLRenderer...");
        this.framework = framework;
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle("amCGAla Framework GL");
            Display.setInitialBackground(1f, 1f, 1f);
            PixelFormat pf = new PixelFormat().withSamples(5);
            Display.create(pf);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, width, height, 0, 1, -1);
            glMatrixMode(GL_MODELVIEW);
        } catch (LWJGLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void setTransformationMatrix(Matrix transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    @Override
    public void drawPixel(Pixel pixel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawPixel(Pixel pixel, Color color) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Pixel getPixel(Vector3d vector) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setColor(Color color) {
        float[] colorComp = color.getColorComponents(null);
        glColor3f(colorComp[0], colorComp[1], colorComp[2]);
        currentColor = color;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawCircle(double x, double y, double radius) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void show() {
        if (!Display.isCloseRequested()) {
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
            Display.update();
        } else {
            Display.destroy();
        }
    }

    @Override
    public void drawLine(Vector3d start, Vector3d end) {
        Pixel s = camera.getImageSpaceCoordinates(start);
        Pixel e = camera.getImageSpaceCoordinates(end);
        glBegin(GL_LINE_STRIP);
        glVertex2f(s.getX(), s.getY());
        glVertex2f(e.getX(), s.getY());
        glEnd();
    }

    @Override
    public void drawCircle(Vector3d pos, double radius) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawPixel(Vector3d point, Color color) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFrame(JFrame frame) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillRect(Pixel pos, int width, int height, Color color) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Color getColor() {
        return currentColor;
    }
}
