package amcgala.example.gameoflife;

import amcgala.Framework;

public class GameOfLifeMain extends Framework{

	public GameOfLifeMain(int width, int height) {
		super(width, height);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameOfLifeMain gol = new GameOfLifeMain(250, 250);
		gol.start();
		gol.show();

	}

	@Override
	public void initGraph() {
		Field field = new Field(10,-110,-120);
		registerInputEventHandler(field);
		add(field);
	}

}
