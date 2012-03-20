package amcgala.example.gameoflife;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.Polygon;
import amcgala.framework.shape.Shape;

public class Element extends Shape {

	private Polygon polygon;
	public boolean isAlive;

	public Element(int width, int height, double x, double y) {
		this.isAlive = false;
		polygon = new Polygon(new Vector3d(x, y, 0), new Vector3d(x + width, y,
				0), new Vector3d(x+width, y + height, 0), new Vector3d(x , y
				+ height, 0), new Vector3d(0, 0, 0));
	}

	@Override
	public void render(Matrix arg0, Camera arg1, Renderer arg2) {
		if (this.isAlive) {
			polygon.render(arg0, arg1, arg2);
		}
	}

}
