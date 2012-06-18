package org.amcgala.framework.animation;

import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.Shape;

/**
 * Eine {@code Animation} kann einem {@link org.amcgala.framework.shape.Shape} hinzugefügt werden, um dieses über die Zeit
 * hinweg verändern zu können.
 */
public interface Animation<T extends Shape> extends Updatable {
    /**
     * Gibt das {@link Shape} zurück, das von der AbstractAnimation beeinflusst wird.
     *
     * @return das Shapeobjekt
     */
    T getShape();

    /**
     * Ändert das Shapeobjekt, das von der AbstractAnimation beeinflusst wird.
     *
     * @param shape das Shape, zu dem diese Animation gehört.
     */
    void setShape(T shape);


}
