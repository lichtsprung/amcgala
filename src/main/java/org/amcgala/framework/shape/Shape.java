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
import org.amcgala.framework.appearance.Appearance;
import org.amcgala.framework.appearance.BasicAppearance;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.lighting.Light;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Color;
import org.amcgala.framework.renderer.Renderer;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Diese Klasse stellt die Oberklasse aller darstellbaren Objekte dar.
 *
 * @author Robert Giacinto
 */
public abstract class Shape implements Updatable {

    private static final Logger logger = Logger.getLogger(Shape.class.getName());
    private Animation animation;
    public Color color = Color.BLACK;
    private boolean rendering;
    protected Appearance appearance = new BasicAppearance();

    /**
     * Gibt den Renderstatus des Shapes zurück.
     *
     * @return <code>true</code> wenn Shape gerade gerendert wird
     */
    public boolean isRendering() {
        return rendering;
    }

    /**
     * Wird vom Framework verwendet, um anzuzeigen, dass dieses Shape gerade gerendert wird.
     *
     * @param rendering der Renderstatus dieses Shapes
     */
    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

    /**
     * Diese Methode gibt das Shapeobjekt aus. Es wird von allen Unterklassen implementiert.
     *
     * @param transformation die Transformationsmatrix, die aus den Transformationsgruppen resultiert
     * @param camera         die Kamera der Szene
     * @param renderer       der Renderer
     * @param lights 		 die Lichter innerhalb der Szene
     */
    public abstract void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights);

    /**
     * Setzt die Animation, die auf das Shape angewendet werden soll.
     *
     * @param animation die Animation
     */
    public void setAnimation(Animation animation) {
        this.animation = animation;
        this.animation.setShape(this);
    }

    /**
     * Gibt die Animation zurück, die in dem Shape registriert ist. Animation kann <code>null</code> sein, wenn das Shape keine Animation besitzt.
     * TODO Sollte vielleicht eine leere Animation statt <code>null</code> zurückgegeben werden?
     *
     * @return die aktuelle Animation
     */
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void update() {
    }
    
    /**
     * Übernimmt die übergebenen Oberflächeneigenschaften für dieses Shapeobjekt.
     * @param app Das Appearance-Objekt das die Oberflächeneigenschaften enthält.
     */
    public void setAppearance(Appearance app) {
    	this.appearance = app;
    }
    
    /**
     * Gibt das Appearance-Objekt zurück, das die Oberflächeneigenschaften für dieses Shapeobjekt enthält.
     * @return Das Appearance-Objekt das die Oberflächeneigenschaften enthält.
     */
    public Appearance getAppearance() {
    	return this.appearance;
    }
}
