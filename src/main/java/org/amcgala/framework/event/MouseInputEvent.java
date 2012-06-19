package org.amcgala.framework.event;

import java.awt.event.MouseEvent;

/**
 * Interface aller MouseInputEvents.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public abstract class MouseInputEvent {
    private MouseEvent event;

    public MouseInputEvent(MouseEvent event) {
        this.event = event;
    }

    public int getButton() {
        return event.getButton();
    }
}
