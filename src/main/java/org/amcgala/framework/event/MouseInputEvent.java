package org.amcgala.framework.event;

import org.amcgala.Framework;

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

    public int getX() {
        return event.getX() - Framework.getInstance().getWidth() / 2;
    }

    public int getY() {
        return (Framework.getInstance().getHeight() / 2) - event.getY();
    }
}
