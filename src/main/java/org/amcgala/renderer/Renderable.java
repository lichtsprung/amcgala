package org.amcgala.renderer;

/**
 * Interface, das von allen Objekten implementiert wird, die über einen DefaultRenderer dargestellt werden können.
 *
 * @author Robert Giacinto
 */
public interface Renderable {
    DisplayList getDisplayList(DisplayList list);
}
