package org.amcgala.event;

import java.awt.event.MouseEvent;

/**
 * Event,das anzeigt, das ein Mousebutton losgelassen wurde.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public final class MouseReleasedEvent extends MouseInputEvent {
    public MouseReleasedEvent(MouseEvent event) {
        super(event);
    }
}
