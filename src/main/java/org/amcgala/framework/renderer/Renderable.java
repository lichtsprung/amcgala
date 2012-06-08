package org.amcgala.framework.renderer;

/**
 * Interface, das von allen Objekten implementiert wird, die über einen Renderer dargestellt werden können.
 *
 * @author Robert Giacinto
 */
public interface Renderable {
    void render(Renderer renderer);
}
