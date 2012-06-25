package org.amcgala.example.turtle;

import org.amcgala.TurtleMode;

/**
 * Kleines Beispiel eines Turtleprogramms, das ein Dreieck zeichnet.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class BasicTurtle extends TurtleMode {

    @Override
    public void turtleCommands() {
        set(-100, -100);
        turnLeft(90);


        for(int length = 200; length > 5; length *= 0.95){
            move(length);
            turnRight(90);
        }
    }

    public static void main(String[] args) {
        new BasicTurtle();
    }

}
