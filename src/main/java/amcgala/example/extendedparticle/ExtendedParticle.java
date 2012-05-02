package amcgala.example.extendedparticle;

import amcgala.Framework;
import amcgala.framework.shape.shape2d.Text;

/**
 * Komplexeres Partikelbeispiel.
 * 
 * @author Steffen Troester
 * 
 */
public class ExtendedParticle extends Framework {

	public ExtendedParticle(int width, int height) {
		super(width, height);
	}

	public static void main(String[] args) {
		new ExtendedParticle(500, 500).start();
	}

	@Override
	public void initGraph() {
		
		ParticleShip shape = new ParticleShip();
		this.registerInputEventHandler(shape);
		add(shape);
		add(new Text("< - Leertaste - >", -150, +205));
	}
}
