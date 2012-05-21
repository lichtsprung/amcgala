package org.amcgala.framework.shape.util;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Renderer;

/**
 * Abstrakte Partikel Manipulation die sich spaeter in Gravitation Wind und
 * weiteren Features wiederspiegeln wird.
 * 
 * @author Steffen Troester
 * 
 */
public interface ParticleManipulation {
	/**
	 * Befindet sich der Partikel im Bereich ?
	 * 
	 * @param x
	 *            Position
	 * @param y
	 *            Position
	 * @return
	 */
	public boolean fitInRange(double x, double y);

	/**
	 * manipulieren durch den Manipulator
	 * 
	 * @param p
	 *            Particle
	 */
	public void manipulate(Particle p);

	/**
	 * Rendermethode falls benoetigt
	 * 
	 * @param transformation
	 * @param camera
	 * @param renderer
	 */
	public void render(Matrix transformation, Camera camera, Renderer renderer);
}
