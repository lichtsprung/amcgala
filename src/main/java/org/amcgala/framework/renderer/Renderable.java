package org.amcgala.framework.renderer;

/**
 * Interface, das von allen Objekten implementiert wird, die über einen DefaultRenderer dargestellt werden können.
 *
 * @author Robert Giacinto
 */
public interface Renderable {
    @Deprecated
    void render(Renderer renderer);

    DisplayList getDisplayList();
}
