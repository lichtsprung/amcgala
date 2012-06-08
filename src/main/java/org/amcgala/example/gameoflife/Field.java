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
package org.amcgala.example.gameoflife;

import com.google.common.eventbus.Subscribe;
import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;

import java.awt.event.KeyEvent;
import java.util.Random;

public class Field extends Shape implements InputHandler {

    private static final int GOL_BORDER = 3;
    private static final int ELEMENT_SIZE = 20;
    Element[][] elements;
    int size;

    public Field(int size, int xPos, int yPos) {
        Random r = new Random();
        this.size = size;
        elements = new Element[size][];
        for (int x = 0; x < size; x++) {
            elements[x] = new Element[size];
            for (int y = 0; y < size; y++) {
                elements[x][y] = new Element(ELEMENT_SIZE, ELEMENT_SIZE, xPos
                        + x * (ELEMENT_SIZE + 2), yPos + y * (ELEMENT_SIZE + 2));
                elements[x][y].isAlive = r.nextInt(4) == 2;
            }
        }
        this.setAnimation(new FieldAnimation());
    }

    @Subscribe
    public void nextGeneration(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_RELEASED) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    int count = getNeightborCount(x, y);
                    if (count >= GOL_BORDER) {
                        if (count == GOL_BORDER) {
                            elements[x][y].isAlive = true;
                        }
                        if (count > GOL_BORDER + 2) {
                            elements[x][y].isAlive = false;
                        }

                    } else {
                        elements[x][y].isAlive = false;
                    }
                }
            }
        }
    }

    private int getNeightborCount(int x, int y) {
        int count = 0;

        int nX = (((x - 1) < 0) ? size - 1 : x - 1);
        int nY = (((y - 1) < 0) ? size - 1 : y - 1);

        count += (elements[(x + 1) % size][(y) % size].isAlive) ? 1 : 0;
        count += (elements[(nX) % size][(y) % size].isAlive) ? 1 : 0;
        count += (elements[(x + 1) % size][(y + 1) % size].isAlive) ? 1 : 0;
        count += (elements[(nX) % size][(y + 1) % size].isAlive) ? 1 : 0;
        count += (elements[(x + 1) % size][(nY) % size].isAlive) ? 1 : 0;
        count += (elements[(nX) % size][(nY) % size].isAlive) ? 1 : 0;
        count += (elements[(x) % size][(y + 1) % size].isAlive) ? 1 : 0;
        count += (elements[(x) % size][(nY) % size].isAlive) ? 1 : 0;

        return count;
    }

    @Override
    public void render(Renderer renderer) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                elements[x][y].render(renderer);
            }
        }
    }

    private class FieldAnimation extends Animation<Field> {

        @Override
        public void animate() {

        }
    }
}
