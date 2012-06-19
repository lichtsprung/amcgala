package org.amcgala.framework.event;

import java.awt.event.KeyEvent;

/**
 * Event, das auftritt, wenn eine Taste gedrückt wurde.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public final class KeyPressedEvent extends KeyInputEvent {


    public KeyPressedEvent(KeyEvent event) {
        super(event);
    }
}
