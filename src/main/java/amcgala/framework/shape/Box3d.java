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
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eine Box im 3d Raum.
 *
 * @author Robert Giacinto
 */
public class Box3d extends Shape {

    private Container lines;
    private Vector3d position;
    private double width;
    private double height;
    private double depth;

    /**
     * Der Konstruktor der 3D Box.
     * @param position
     * @param width
     * @param height
     * @param depth
     */
    public Box3d(Vector3d position, double width, double height, double depth) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.depth = depth;
        lines = new Container();
        this.calculate();
    }
    
    private void calculate() {
        lines.add(new Vector3d(position.x, position.y, position.z), new Vector3d(position.x + width, position.y, position.z));
        lines.add(new Vector3d(position.x, position.y, position.z), new Vector3d(position.x, position.y + height, position.z));
        lines.add(new Vector3d(position.x, position.y, position.z), new Vector3d(position.x, position.y, position.z - depth));
        lines.add(new Vector3d(position.x, position.y + height, position.z), new Vector3d(position.x + width, position.y + height, position.z));
        lines.add(new Vector3d(position.x + width, position.y, position.z), new Vector3d(position.x + width, position.y + height, position.z));
        lines.add(new Vector3d(position.x, position.y + height, position.z), new Vector3d(position.x, position.y + height, position.z - depth));
        lines.add(new Vector3d(position.x + width, position.y + height, position.z), new Vector3d(position.x + width, position.y + height, position.z - depth));
        lines.add(new Vector3d(position.x + width, position.y, position.z), new Vector3d(position.x + width, position.y, position.z - depth));
        lines.add(new Vector3d(position.x, position.y, position.z - depth), new Vector3d(position.x + width, position.y, position.z - depth));
        lines.add(new Vector3d(position.x, position.y, position.z - depth), new Vector3d(position.x, position.y + height, position.z - depth));
        lines.add(new Vector3d(position.x, position.y + height, position.z - depth), new Vector3d(position.x + width, position.y + height, position.z - depth));
        lines.add(new Vector3d(position.x + width, position.y, position.z - depth), new Vector3d(position.x + width, position.y + height, position.z - depth));
    }
    
    /**
     * Gibt die Position der Box in Form eines Vektors zurück.
     * @return Vector3d
     */
    public Vector3d getPosition() {
    	return this.position;
    }
    
    /**
     * Setzt die Box auf die übergebene Position.
     * @param position
     */
    public void setPosition(Vector3d position) {
    	this.position = position;
    	lines = new Container();
    	calculate();
    }
    
    /**
     * Gibt die Breite der Box zurück.
     * @return
     */
    public double getWidth() {
    	return this.width;
    }
    
    /**
     * Setzt die Breite der Box auf den übergebenen Wert.
     * @param width
     */
    public void setWidth(double width) {
    	this.width = width;
    }
    
    /**
     * Gibt die Höhe der Box zurück.
     * @return
     */
    public double getHeight() {
    	return this.height;
    }
    
    /**
     * Setzt die Höhe der Box auf den übergebenen Wert.
     * @param height
     */
    public void setHeight(double height) {
    	this.height = height;
    }
    
    /**
     * Gibt die Tiefe der Box zurück.
     * @return
     */
    public double getDepth() {
    	return this.depth;
    }
    
    /**
     * Setzt die Tiefe der Box auf den übergebenen Wert.
     * @param depth
     */
    public void setDepth(double depth) {
    	this.depth = depth;
    }
    
    /**
     *
     */
    @Override
    public void render(Matrix transformation, Camera camera, Renderer renderer) {
    	lines.color = color;
    	lines.render(transformation, camera, renderer);
    }
    
    /**
     * Überschreibt die <i>toString</i> Methode der Klasse <i>Object</i> und gibt die Basiswerte der Box3d zurück.
     */
    public String toString() {
    	String ausgabe = "";
    	ausgabe += "Box3d { x = ";
    	ausgabe += this.position.x;
    	
    	ausgabe += "; y = ";
    	ausgabe += this.position.y;
    	
    	ausgabe += "; z = ";
    	ausgabe += this.position.z;
    	
    	ausgabe += "; width = ";
    	ausgabe += this.width;
    	
    	ausgabe += "; height = ";
    	ausgabe += this.height;
    	
    	ausgabe += "; depth = ";
    	ausgabe += this.depth + " };";
    	
    	return ausgabe;
    }
    
    private static final Logger log = LoggerFactory.getLogger(Box3d.class);
}
