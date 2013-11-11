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
package org.amcgala.renderer;

import org.amcgala.Framework;
import org.amcgala.camera.Camera;
import org.amcgala.event.*;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.PointPrimitive;
import org.amcgala.shape.primitives.RectanglePrimitive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Der Renderer in einer Software Implementierung ohne Hardwarebeschleunigung. Es wird ein JFrame verwendet, in dem über ein Graphics Object
 * auf einem Panel pixelweise gezeichnet wird.
 */
public class SoftwareRenderer implements Renderer {

    /**
     * Die Breite der Ausgabe des Renderers
     */
    private int width;
    /**
     * Die Höhe der Ausgabe des Renderers
     */
    private int height;
    /**
     * Die BufferStrategy wird für das Double Buffering verwendet des Frames verwendet.
     */
    private BufferStrategy bs;
    /**
     * Der Frame, in dem der Software Renderer zeichnet.
     */
    private JFrame frame;
    /**
     * Das {@link Graphics} Objekt, das für das Zeichnen auf dem Panel verwendet wird.
     */
    private Graphics g;
    /**
     * Die globale Instanz des Frameworks kann verwendet werden, um vom Renderer Einfluss auf
     * das laufende Programm nehmen zu können.
     */
    private Framework framework = Framework.getInstance();


    /**
     * Der SoftwareRenderer initialisiert sich mit den Properties, die aus der laufenden Framework Instanz genommen werden.
     */
    public SoftwareRenderer() {
        this.width = framework.getWidth();
        this.height = framework.getHeight();


        frame = new JFrame("Software Renderer");
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else {
                    if (framework.hasActiveScene()) {
                        framework.getActiveScene().getEventBus().post(new KeyPressedEvent(e));
                    }
                    framework.getEventBus().post(new KeyPressedEvent(e));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(new KeyReleasedEvent(e));
                }

                framework.getEventBus().post(new KeyReleasedEvent(e));
            }
        });

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(new MouseClickedEvent(e));
                }

                framework.getEventBus().post(new MouseClickedEvent(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(new MousePressedEvent(e));
                }

                framework.getEventBus().post(new MousePressedEvent(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(new MouseReleasedEvent(e));
                }

                framework.getEventBus().post(new MouseReleasedEvent(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(e);
                }

                framework.getEventBus().post(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(e);
                }

                framework.getEventBus().post(e);
            }
        });

        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(e);
                }

                framework.getEventBus().post(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(e);
                }

                framework.getEventBus().post(e);
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (framework.hasActiveScene()) {
                    framework.getActiveScene().getEventBus().post(e);
                }

                framework.getEventBus().post(e);
            }
        });

        frame.setBackground(Color.WHITE);
        frame.setUndecorated(true);
        frame.setLocation(150, 80);
        frame.setVisible(true);
        frame.createBufferStrategy(2);
        bs = frame.getBufferStrategy();
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setColor(Color color) {
        g.setColor(checkNotNull(color));
    }


    @Override
    public void render() {
        bs.show();
        g = bs.getDrawGraphics();
        g.setColor(frame.getBackground());
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        if (Framework.getInstance().hasActiveScene()) {
            DisplayList list = framework.getCurrentState();
            Camera camera = Framework.getInstance().getActiveScene().getCamera();

            for (LinePrimitive line : list.lines) {
                Pixel start = camera.getImageSpaceCoordinates(line.v0.toVector());
                Pixel end = camera.getImageSpaceCoordinates(line.v1.toVector());
                g.setColor(line.color.toAWTColor());
                g.drawLine(start.x, start.y, end.x, end.y);
            }

            for (PointPrimitive point : list.points) {
                Pixel p = camera.getImageSpaceCoordinates(point.point.toVector());
                g.setColor(point.color.toAWTColor());
                g.fillRect(p.x, p.y, 1, 1);
            }

            for (RectanglePrimitive r : list.rects) {
                Pixel start = camera.getImageSpaceCoordinates(r.v0.toVector());
                g.setColor(r.color.toAWTColor());
                g.fillRect(start.x, start.y, (int) r.width, (int) r.height);
            }
        }
    }

    @Override
    public Color getColor() {
        return g.getColor();
    }

}
