package amcgala.example.clock;

import amcgala.Framework;

public class ClockMain extends Framework {

	public ClockMain(int width, int height) {
		super(width, height);
	}

	@Override
	public void initGraph() {
		add(new Clock());
	}
	
	public static void main(String[] args) {
		ClockMain m = new ClockMain(400, 400);
		m.start();
		m.show();
	}

}
