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
package org.amcgala.animation;

import org.amcgala.Framework;
import org.amcgala.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Diese Klasse kümmert sich um das Timing der Animation. Sie ruft in
 * regelmäßigen Abständen die Methoden update und show der Klasse Framework auf
 * und ermöglicht so die Realisation von Animationen.
 *
 * @author Robert Giacinto
 */
public class Animator extends Thread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Animator.class);
    private Timer fpsTimer;
    private Timer upsTimer;
    private Framework framework;
    private Renderer renderer;
    private int framesPerSecond;
    private int updatesPerSecond;
    private Thread animation;
    private boolean running;
    private Class<? extends Renderer> rendererClass;

    /**
     * Erzeugt einen neuen Animator, der das Framework aktualisiert.
     *
     * @param framesPerSecond  die Anzahl der Bilder pro Sekunde
     * @param updatesPerSecond die Anzahl der Aktualisierungen pro Sekunde
     */
    public Animator(int framesPerSecond, int updatesPerSecond, Framework framework, Class<? extends Renderer> renderer) {
        checkArgument(framesPerSecond > 0, "FPS muss größer 0 sein!");
        checkArgument(updatesPerSecond > 0, "UPS muss größer 0 sein!");
        checkArgument(framework != null, "Framework darf nicht null sein!");
        checkArgument(renderer != null, "Renderer darf nicht null sein!");

        this.framework = framework;

        this.rendererClass = renderer;

        this.framesPerSecond = framesPerSecond;
        this.updatesPerSecond = updatesPerSecond;
        fpsTimer = new Timer(framesPerSecond);
        upsTimer = new Timer(updatesPerSecond);
        animation = new Thread(this);
        start();
    }

    /**
     * Gibt die Anzahl der Aktualisierungen zurück.
     *
     * @return die Anzahl der Aktualsierungen pro Sekunde
     */
    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * Ändert die Anzahl der Aktualisierungen pro Sekunde.
     *
     * @param framesPerSecond die neue Anzahl von Aktualisierungen pro Sekunde
     */
    public void setFramesPerSecond(int framesPerSecond) {
        checkArgument(framesPerSecond > 0, "FPS muss größer 0 sein!");

        this.framesPerSecond = framesPerSecond;
        fpsTimer = new Timer(framesPerSecond);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        try {
            renderer = rendererClass.getDeclaredConstructor().newInstance();
            log.info("New Renderer: {}", renderer);
            running = true;
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        double fpsLastTime = System.nanoTime();
        double upsLastTime = System.nanoTime();
        int upsCounter = 0;
        int fpsCounter = 0;
        int unprocessed = 1;
        double last = System.nanoTime();
        boolean render = true;
        while (running) {
            double now = System.nanoTime();
            unprocessed += (now - last) / upsTimer.getTimePerFrame();

            fpsTimer.start();
            while (unprocessed > 0) {
                upsCounter++;
                unprocessed--;
                framework.update();
                last = System.nanoTime();
                render = true;
            }


            if (render) {
                fpsCounter++;
                renderer.show();
                render = false;
            }
            fpsTimer.stop();
            try {
                Thread.sleep((long) fpsTimer.getSleepTime());
            } catch (InterruptedException e) {
            }

            if (System.nanoTime() - fpsLastTime > 1000000000) {
                log.info("FPS = {}", fpsCounter);
                fpsCounter = 0;
                fpsLastTime = System.nanoTime();
            }

            if (System.nanoTime() - upsLastTime > 1000000000) {
                log.info("UPS = {}", upsCounter);
                upsCounter = 0;
                upsLastTime = System.nanoTime();
            }
        }
    }
}
