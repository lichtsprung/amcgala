package amcgala.framework.shape.util;

import amcgala.framework.animation.Updatable;
import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.Shape;
import amcgala.framework.shape.shape2d.Point2d;

/**
 * Partikel Element fuer den ParticleEmitter. TODO: Es sollte hier moeglich sein
 * auch verschiedene Objekte als Particle zu nutzen !
 * 
 * @author Steffen Troester
 * 
 */
public class Particle extends Shape implements Updatable {

	private double particleSpeed;
	private Vector3d direction;

	private Point2d point2d;

	/**
	 * Particle Constructor initialisiert vom ParticleEmitter
	 * 
	 * @param particleSpeed
	 * @param direction
	 * @param x
	 * @param y
	 */
	public Particle(double particleSpeed, final Vector3d direction, double x,
			double y) {
		this.particleSpeed = particleSpeed;
		this.direction = direction.normalize();

		this.point2d = new Point2d(x, y);
		Vector3d v  = this.direction.copy();
		v.times(particleSpeed);
	}

	@Override
	public void update() {
		point2d.x += direction.x;
		point2d.y += direction.y;
		super.update();
	}

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer) {
		point2d.render(transformation, camera, renderer);
	}

	public Vector3d getDirection() {
		return direction;
	}

	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

	public double getParticleSpeed() {
		return particleSpeed;
	}

	public void setParticleSpeed(double particleSpeed) {
		this.particleSpeed = particleSpeed;
	}

	public double getX() {
		return point2d.x;
	}

	public double getY() {
		return point2d.y;
	}

}
