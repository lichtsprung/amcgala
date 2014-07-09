package org.amcgala.renderer;

import org.amcgala.scenegraph.transform.Transformation;

/**
 * Interface, das von allen Objekten implementiert wird, die über einen DefaultRenderer dargestellt werden können.
 *
 * @author Robert Giacinto
 */
public interface Renderable {
    DisplayList getDisplayList(DisplayList list);
    DisplayList getDisplayList(DisplayList list, Transformation transformation);
}
