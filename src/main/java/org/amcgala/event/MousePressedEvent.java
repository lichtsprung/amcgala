package org.amcgala.event;

import java.awt.event.MouseEvent;

/**
 * Event, das anzeigt, dass ein Mouse Button gedrückt ist.
 */
public final class MousePressedEvent extends MouseInputEvent {

    public MousePressedEvent(MouseEvent event) {
        super(event);
    }
}
