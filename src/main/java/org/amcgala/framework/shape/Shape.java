/* 
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.amcgala.framework.shape;

import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.renderer.Renderable;

import java.awt.Color;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Diese Klasse stellt die Oberklasse aller darstellbaren Objekte dar.
 *
 * @author Robert Giacinto
 */
public abstract class Shape implements Updatable, Renderable {

    private static final Logger logger = Logger.getLogger(Shape.class.getName());
    private Animation animation = Animation.EMPTY_ANIMATION;
    private Color color = Color.BLACK;

    /**
     * Setzt die Animation, die auf das Shape angewendet werden soll.
     *
     * @param animation die Animation
     */
    public void setAnimation(Animation animation) {
        this.animation = checkNotNull(animation);
        this.animation.setShape(this);

    }

    /**
     * Gibt die Animation zurück, die in dem Shape registriert ist.
     *
     * @return die aktuelle Animation
     */
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void update() {
        animation.update();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = checkNotNull(color);
    }
}
