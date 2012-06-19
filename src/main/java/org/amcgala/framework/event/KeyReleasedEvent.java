package org.amcgala.framework.event;

import java.awt.event.KeyEvent;

/**
 * Event, das gefeuert wird, wenn eine Taste losgelassen wird.
 */
public final class KeyReleasedEvent extends KeyInputEvent {


    public KeyReleasedEvent(KeyEvent event) {
        super(event);
    }
}
