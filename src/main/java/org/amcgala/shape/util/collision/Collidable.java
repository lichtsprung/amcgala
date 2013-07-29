package org.amcgala.shape.util.collision;

import java.util.Collection;

/**
 * Interface, das von allen {@link org.amcgala.shape.Shape} Objekten implementiert wird, zwischen denen Kollisionen
 * bestimmt werden sollen.
 *
 * @author Robert Giacinto
 */
public interface Collidable {

    /**
     * Berechnet die Kollision zwischen dem {@link org.amcgala.shape.Shape} und einem anderen Objekt.
     *
     * @param other das andere Objekt
     * @return die Menge aller Kollisionen zwischen den beiden Objekten
     */
    Collection<Collision> collideWith(Collidable other);
}
