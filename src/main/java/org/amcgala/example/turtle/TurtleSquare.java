package org.amcgala.example.turtle;

import org.amcgala.TurtleMode;

/**
 * Ein Beispiel für die Verwendung der Turtleklasse und der Funktionalitäten, die sie bietet.
 *
 * @author Robert Giacinto
 */
public class TurtleSquare extends TurtleMode {

    @Override
    public void turtleCommands() {
        double length = 300;

        gotoStart(length);

        while(length > 50){
            newSquare(length);
            gap(25);
            length -= 50;
            turnLeft(5);
        }
    }

    private void gap(double length) {
        up();
        move(length);
        turnRight(90);
        move(length);
        turnLeft(90);
        down();
    }

    private void gotoStart(double width){
        up();
        turnLeft(180);
        move(width/2);
        turnRight(90);
        move(width/2);
        turnRight(90);
        down();
    }

    private void newSquare(double length) {
        move(length);
        turnRight(90);
        move(length);
        turnRight(90);
        move(length);
        turnRight(90);
        move(length);
        turnRight(90);
    }

    public static void main(String[] args) {
        new TurtleSquare();
    }
}
