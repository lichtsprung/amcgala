package amcgala.framework.shape.util;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.Shape;
import amcgala.framework.shape.shape2d.Rectangle;

/**
 * Partikel sollen im definierten Bereich mehr Gravitation bekommen.
 * 
 * @author Steffen Troester
 * 
 */
public class ParticleGravitation extends Shape implements ParticleManipulation {

	private Rectangle rectangle;
	private final double x;
	private final double y;
	private final double width;
	private final double height;
	private double gravitation = 0.01;
	private boolean visible;

	/**
	 * ParticleGravitation Constructor definiert den Bereich der Gravitation
	 * 
	 * @param x
	 *            Position
	 * @param y
	 *            Position
	 * @param width
	 *            groesser 0 !
	 * @param height
	 *            groesser 0 !
	 */
	public ParticleGravitation(double x, double y, double width, double height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rectangle = new Rectangle(x, y, width, height);
	}

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer) {
		if (isVisible()) {
			rectangle.render(transformation, camera, renderer);
		}
	}

	@Override
	public boolean fitInRange(double x, double y) {
		return (x - this.x < width && x - this.x > 0 && y - this.y < height && y
				- this.y > 0);
	}

	@Override
	public void manipulate(Particle p) {
		Vector3d direction = p.getDirection().copy();
		direction.y -= gravitation;
		p.setDirection(direction);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setGravitation(double gravitation) {
		this.gravitation = gravitation;
	}

	public double getGravitation() {
		return gravitation;
	}
}
