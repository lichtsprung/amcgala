package org.amcgala;

import org.amcgala.math.Vector3d;

/**
 * Der Zustand (Position, Ausrichtung) einer Turtle. Diese Klasse wird dafür verwendet, um die notwendigen Zustandsvariablen
 * zwischen Kopien von Turtles transportieren zu können.
 */
public class TurtleState {
    protected double headingAngle;
    protected Vector3d heading;
    protected Vector3d position;

    public TurtleState(double headingAngle, Vector3d heading, Vector3d position) {
        this.headingAngle = headingAngle;
        this.heading = heading;
        this.position = position;
    }
}
