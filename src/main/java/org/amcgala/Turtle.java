package org.amcgala;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.Line;
import org.amcgala.framework.shape.util.CompositeShape;

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
    private Vector3d heading = Vector3d.UNIT_Y;

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
        headingAngle = 90;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
    }

    /**
     * Erzeugt eine neue Turtle, die mit beliebigen Werten initialisiert werden kann.
     *
     * @param position die Position der Turtle
     * @param heading der Vektor der Blickrichtung der Turtle
     * @param headingAngle der Blickwinkel
     * @param shape das Shape, in dem die Grafik gespeichert werden soll
     */
    public Turtle(Vector3d position, Vector3d heading, double headingAngle, CompositeShape shape) {
        this.position = position;
        this.heading = heading;
        this.turtleShape = shape;
        this.headingAngle = headingAngle;
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
     * @param angle der Winkel in Grad
     */
    public void turnLeft(double angle) {
        headingAngle += angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    /**
     * Dreht die Turtle um einen bestimmten Winkel im Uhrzeigersinn.
     * @param angle der Winkel in Grad
     */
    public void turnRight(double angle) {
        headingAngle -= angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    /**
     * Lässt die Turtle geradeaus gehen.
     * @param length die Länge des Schritts
     */
    public void move(double length) {
        checkArgument(length > 0, "Schrittlänge kann nur positiv sein!");

        if (up) {
            position = position.add(heading.times(length));
        } else {
            Vector3d endPosition = position.add(heading.times(length));
            endPosition.z = -1;
            position.z = -1;
            Line line = new Line(position, endPosition);
            turtleShape.add(line);
            position = endPosition;
        }
    }

    /**
     * Gibt die Blickrichtung der Turtle zurück.
     * @return die Blickrichtung der Turtle
     */
    public Vector3d getHeading() {
        return heading;
    }

    /**
     * Gibt die Position der Turtle zurück.
     * @return die Position der Turtle
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Gibt den Blickwinkel der Turtle zurück.
     * @return der Blickwinkel der Turtle
     */
    public double getHeadingAngle() {
        return headingAngle;
    }
}
