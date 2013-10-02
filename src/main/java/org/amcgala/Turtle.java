package org.amcgala;

import org.amcgala.math.Vector3d;
import org.amcgala.shape.Line;
import org.amcgala.shape.util.CompositeShape;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.*;

/**
 * Eine Turtle kann zum Zeichnen von Turtlegrafiken innerhalb eines amCGAla Programms verwendet werden.
 *
 * @author Robert Giacinto
 */
public class Turtle {

    // In diesem Shape werden die einzelnen Linienobjekte, die von der Turtle erzeugt werden, gespeichert und geschlossen
    // gerendert.
    private CompositeShape turtleShape;

    // Die Blickrichtung der Turtle. Zu Beginn schaut die Turtle nach oben.
    private Vector3d heading = Vector3d.UNIT_X;

    // Die Turtle steht im Nullpunkt des Koordinatensystems. Aktuell ist dies der Bildmittelpunkt.
    private Vector3d position = Vector3d.ZERO;

    // Der Blinkwinkel - das gleiche wie heading, nur dass es sich hierbei um eine Graddarstellung im Bogenmaß handelt.
    private double headingAngle;

    // Sitzt der Stift der Turtle auf? Ist up true, dann zeichnet die Turtle nicht.
    private boolean up;


    /**
     * Erzeugt ein neues Turtle-Objekt, das die Grafik in dem übergebenem Shape speichert.
     *
     * @param shape das Shape, in dem die Turtlegrafik gespeichert werden soll
     */
    public Turtle(CompositeShape shape) {
        turtleShape = shape;
        headingAngle = 0;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), 0);
    }

    /**
     * Erzeugt eine neue Turtle, die mit beliebigen Werten initialisiert werden kann.
     *
     * @param turtleState der Startzustand der Turtle
     * @param shape       das Shape, in dem die Grafik gespeichert werden soll
     */
    public Turtle(TurtleState turtleState, CompositeShape shape) {
        this.position = turtleState.position;
        this.heading = turtleState.heading;
        this.headingAngle = turtleState.headingAngle;
        this.turtleShape = shape;
    }


    /**
     * Beendet den Zeichenmodus der Turtle.
     */
    public void up() {
        up = true;
    }

    /**
     * Setzt die Turtle in den Zeichenmodus.
     */
    public void down() {
        up = false;
    }

    /**
     * Dreht die Turtle um einen bestimmten Winkel gegen den Uhrzeigersinn..
     *
     * @param angle der Winkel in Grad
     */
    public void turnLeft(double angle) {
        headingAngle -= angle;

    }

    /**
     * Dreht die Turtle um einen bestimmten Winkel im Uhrzeigersinn.
     *
     * @param angle der Winkel in Grad
     */
    public void turnRight(double angle) {
        headingAngle += angle;
    }

    /**
     * Lässt die Turtle geradeaus gehen.
     *
     * @param length die Länge des Schritts
     */
    public void move(double length) {
        checkArgument(length > 0, "Schrittlänge kann nur positiv sein!");
        heading = new Vector3d(cos(toRadians(headingAngle)), -sin(toRadians(headingAngle)), 0);
        heading.normalize();
        if (up) {
            position = position.add(heading.times(length));
        } else {
            Vector3d endPosition = position.add(heading.times(length));
            endPosition.setZ(0);
            position.setZ(0);
            Line line = new Line(position.toVertex3f(), endPosition.toVertex3f());
            turtleShape.add(line);
            position = endPosition;
        }
    }

    public TurtleState getTurtleState() {
        return new TurtleState(headingAngle, heading, position);
    }
}
