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
package org.amcgala.example.concurrency;

import com.google.common.eventbus.Subscribe;
import org.amcgala.example.pong.Cross;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Dieses Shape fügt einer internen, nichtsynchronisierten Liste Instanzen von
 * Cross2d hinzu, um neue Synchronisierungsstrategien testen zu können.
 *
 * @author Robert Giacinto
 */
public class RandomCrossArray extends Shape implements InputHandler {

    private List<Cross> crosses;
    private Random random;

    public RandomCrossArray() {
        crosses = new CopyOnWriteArrayList<Cross>();
        random = new Random(System.nanoTime());
    }

    public void addCross(Cross cross) {
        crosses.add(cross);
    }

    @Override
    public void render(Renderer renderer) {
        for (Cross cross : crosses) {
            cross.render(renderer);
        }
    }

    @Subscribe
    public void handleKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            Cross c2d = new Cross(random.nextInt(800) - 400, random.nextInt(600) - 300, random.nextInt(50));
            c2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            addCross(c2d);
        }
    }
}
