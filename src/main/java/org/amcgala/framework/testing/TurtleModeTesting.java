package org.amcgala.framework.testing;

import org.amcgala.TurtleMode;

/**
 * Turtle Test
 */
public class TurtleModeTesting extends TurtleMode {
    @Override
    public void turtleCommands() {
        up();
        move(400);
        turnRight(90);
        move(300);
        turnLeft(90);
        down();
        move(100);
        turnLeft(90);
        move(50);
    }
}

class Main {
    public static void main(String[] args) {
        new TurtleModeTesting();
    }
}