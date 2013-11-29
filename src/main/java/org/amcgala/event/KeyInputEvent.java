package org.amcgala.event;

import java.awt.event.KeyEvent;

/**
 * Interface des Events, die vom Framework verarbeitet werden können.
 */
public abstract class KeyInputEvent implements Event{
    private KeyEvent event;

    public KeyInputEvent(KeyEvent event) {
        this.event = event;
    }

    public int getKeyCode() {
        return event.getKeyCode();
    }
}
