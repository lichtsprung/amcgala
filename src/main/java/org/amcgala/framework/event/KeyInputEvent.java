package org.amcgala.framework.event;

import java.awt.event.KeyEvent;

/**
 * Interface des Events, die vom Framework verarbeitet werden können.
 */
public abstract class KeyInputEvent {
    private KeyEvent event;

    public KeyInputEvent(KeyEvent event) {
        this.event = event;
    }

    public int getKeyCode() {
        return event.getKeyCode();
    }
}
