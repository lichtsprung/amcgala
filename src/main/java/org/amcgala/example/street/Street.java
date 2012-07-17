package org.amcgala.example.street;

import org.amcgala.TurtleMode;

import java.util.Random;

/**
 * Eine Aufgabe, die mit dem Turtle Mode eine Straße zeichnet.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Street extends TurtleMode {

    @Override
    public void turtleCommands() {
        gotoStreet();
        drawStreet(WIDTH);
        drawHouses(WIDTH);
    }

    private void drawHouses(int length) {
        up();
        turnLeft(90);
        move(length / 4);
        move(length / 16);
        turnRight(90);

        Random random = new Random(System.nanoTime());
        int count = random.nextInt(10) + 3;
        double spacing = 20;
        double houseWidth = (length - (count + 1) * spacing) / count;
        move(spacing);

        for (int i = 0; i < count; i++) {
            drawHouse(houseWidth, random.nextInt(6) + 1);
            up();
            move(spacing + houseWidth);
        }
    }

    private void gotoStreet() {
        up();
        turnLeft(180);
        move(WIDTH / 2);
        turnLeft(90);
        move(HEIGHT / 4);
        turnLeft(90);
    }

    private void drawStreet(double length) {
        // Straße unten
        down();
        move(length);
        up();

        // Straße oben
        turnLeft(90);
        move(length / 4);
        turnLeft(90);
        down();
        move(length);
        up();

        // Mittelstreifen
        turnLeft(90);
        move(length / 8);
        turnLeft(90);

        boolean b = false;
        int stepCount = 10;
        double step = length / stepCount;

        for (double i = 0; i < stepCount; i++) {
            if (b) {
                down();
            } else {
                up();
            }

            move(step);
            b = !b;
        }

        // Zurück nach linksunten
        up();
        turnRight(90);
        move(length / 8);
        turnRight(90);
        move(length);
        turnRight(180);
    }

    private void drawHouse(double width, int floors) {
        Random random = new Random(System.nanoTime());
        double height = random.nextDouble() * 50 + 20;
        int windowCount = random.nextInt(3) + 2;

        for (int i = 0; i < floors + 1; i++) {
            if(i == 0) {
                drawGroundFloor(width, height);
            }else{
                drawFloor(width, height, windowCount);
            }

            turnLeft(90);
            move(height);
            turnRight(90);
        }

        turnRight(90);
        move((floors + 1) * height);
        turnLeft(90);
    }

    private void drawFloor(double width, double height, int windowCount) {
        down();
        move(width);
        turnLeft(90);
        move(height);
        turnLeft(90);
        move(width);
        turnLeft(90);
        move(height);
        turnLeft(90);
        up();
    }

    private void drawGroundFloor(double width, double height){
        double doorWidth = width / 5;
        double doorHeight = height * 0.5;
        down();
        move(width);
        turnLeft(90);
        move(height);
        turnLeft(90);
        move(width);
        turnLeft(90);
        move(height);
        turnLeft(90);
        up();

        move(width/2 - doorWidth/2);
        turnLeft(90);
        down();
        move(doorHeight);
        turnRight(90);
        move(doorWidth);
        turnRight(90);
        move(doorHeight);
        up();
        turnRight(90);
        move(width/2 + doorWidth/2);
        turnRight(180);
    }

    public static void main(String[] args) {
        new Street();
    }
}
