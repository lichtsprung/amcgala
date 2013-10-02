package org.amcgala.event;

import java.awt.event.MouseEvent;

/**
 * Event, das anzeigt, dass ein Mousebutton geklickt wurde.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public final class MouseClickedEvent extends MouseInputEvent {

    public MouseClickedEvent(MouseEvent event) {
        super(event);
    }
}
