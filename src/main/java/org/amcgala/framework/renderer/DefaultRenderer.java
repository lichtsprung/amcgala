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
package org.amcgala.framework.renderer;

import org.amcgala.Framework;
import org.amcgala.framework.event.*;
import org.amcgala.framework.shape.primitives.LinePrimitive;
import org.amcgala.framework.shape.primitives.PointPrimitive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wird von jedem DefaultRenderer erweitert und stellt die Funktionen drawPixel und show zur Verfügung.
 */
public class DefaultRenderer implements Renderer {

    /**
     * Die Breite der Ausgabe des Renderers
     */
    private int width;
    /**
     * Die Höhe der Ausgabe des Renderers
     */
    private int height;
    private BufferStrategy bs;
    private JFrame frame;
    private Graphics g;
    private Framework framework = Framework.getInstance();


    public DefaultRenderer() {
        this.width = framework.getWidth();
        this.height = framework.getHeight();


        frame = new JFrame("Software Renderer");
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                framework.getActiveScene().getEventBus().post(new KeyPressedEvent(e));
                framework.getEventBus().post(new KeyPressedEvent(e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                framework.getActiveScene().getEventBus().post(new KeyReleasedEvent(e));
                framework.getEventBus().post(new KeyReleasedEvent(e));
            }
        });

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(new MouseClickedEvent(e));
                framework.getEventBus().post(new MouseClickedEvent(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(new MousePressedEvent(e));
                framework.getEventBus().post(new MousePressedEvent(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(new MouseReleasedEvent(e));
                framework.getEventBus().post(new MouseReleasedEvent(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(e);
                framework.getEventBus().post(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(e);
                framework.getEventBus().post(e);
            }
        });

        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(e);
                framework.getEventBus().post(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                framework.getActiveScene().getEventBus().post(e);
                framework.getEventBus().post(e);
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                framework.getActiveScene().getEventBus().post(e);
                framework.getEventBus().post(e);
            }
        });

        frame.setBackground(Color.WHITE);

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
    public void show() {
        bs.show();
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        DisplayList list = framework.getCurrentState();

        for (LinePrimitive line : list.lines) {
            g.setColor(line.color);
            g.drawLine((int) line.vertices.get(0).x, (int) line.vertices.get(0).y, (int) line.vertices.get(1).x, (int) line.vertices.get(1).y);
        }

        for (PointPrimitive point : list.points) {
            g.setColor(point.color);
            g.fillRect((int) point.vertices.get(0).x, (int) point.vertices.get(0).y, 1, 1);
        }

        // TODO Gefüllte Dreiecke

        // TODO Gefüllte Vierecke
    }

    @Override
    public Color getColor() {
        return g.getColor();
    }

}
