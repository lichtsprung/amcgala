package org.amcgala;

import org.amcgala.framework.math.Vector3;

/**
 * Der Zustand (Position, Ausrichtung) einer Turtle. Diese Klasse wird dafür verwendet, um die notwendigen Zustandsvariablen
 * zwischen Kopien von Turtles transportieren zu können.
 */
public class TurtleState {
    protected double headingAngle;
    protected Vector3 heading;
    protected Vector3 position;

    public TurtleState(double headingAngle, Vector3 heading, Vector3 position) {
        this.headingAngle = headingAngle;
        this.heading = heading;
        this.position = position;
    }
}
