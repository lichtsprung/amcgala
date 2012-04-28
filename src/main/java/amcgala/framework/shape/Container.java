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
package amcgala.framework.shape;

import amcgala.framework.camera.Camera;
import amcgala.framework.event.InputHandler;
import amcgala.framework.lighting.Light;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Container for Shapeobjects.
 *
 * @author Sascha Lemke
 */
public class Container extends Shape implements InputHandler {

    private ArrayList<Shape> objects;

    /**
     * Creates an empty Containerobject.
     */
    public Container() {
        this.objects = new ArrayList<Shape>();
    }
    
    /**
     * Creates an Containobject with the given shapes.
     * @param object
     */
    public Container(Shape[] object) {
    	this.objects = new ArrayList<Shape>();
    	for(int i = 0; i < object.length; i++) {
    		this.objects.add(object[i]);
    	}
    }

    /**
     * Adds a Shape.
     * @param object
     */
    public void add(Shape object) {
    	this.objects.add(object);
    }
    
    
    /**
     * Adds an array of shapes.
     * @param object
     */
    public void add(Shape[] object) {
    	for(int i = 0; i < object.length; i++) {
    		this.objects.add(object[i]);
    	}
    }

    /**
     * Removes the last entry.
     *
     * @return
     */
    public void remove() {
    	this.objects.remove(this.objects.size() -1);
    }

    /**
     * Removes an selected entry.
     *
     * @param index the selected entry
     * @return
     */
    public void remove(Shape object) {
    	this.objects.remove(object);
    }

    /**
     * Überschreibt die <i>toString</i> Methode der Klasse <i>Object</i> und gibt bei Aufruf die Linien mit den Koordinaten wieder, sowie die Anzahl der Linen und ob diese Inhalt besitzen.
     */
    @Override
    public String toString() {
        String ausgabe = "";
        Iterator<Shape> iter = this.objects.iterator();
        while(iter.hasNext()) {
        	Shape object = iter.next();
        	ausgabe += object.toString();
        	ausgabe += "\n";
        }
        return ausgabe;
    }

    /**
     *
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer, Collection<Light> lights) {
        Iterator<Shape> iter = this.objects.iterator();
        while (iter.hasNext()) {
            Shape object = iter.next();
            object.color = color;
            object.render(transformation, camera, renderer, lights);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(Container.class.getName());
}
