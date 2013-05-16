package org.amcgala;

import org.amcgala.framework.math.Vector3;
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
    private Vector3 heading = Vector3d.UNIT_X;

    private Vector3 position = Vector3d.ZERO;

    // Sitzt der Stift der Turtle auf? Ist up true, dann zeichnet die Turtle nicht.
    private boolean up;


    /**
     * Erzeugt ein neues Turtle-Objekt, das die Grafik in dem übergebenem Shape speichert.
     *
     * @param shape das Shape, in dem die Turtlegrafik gespeichert werden soll
     */
    public Turtle(CompositeShape shape) {
        turtleShape = shape;
        heading = Vector3d.UNIT_X;
    }


    public Turtle(Vector3 position, CompositeShape shape) {
        this(shape);
        this.position = position;
    }

    public Turtle(Vector3 position, Vector3 heading, CompositeShape shape) {
        this.position = position;
        this.heading = heading;
        this.turtleShape = shape;
        System.out.println("new turtle with heading: " + heading + " at position " + position);
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
        Vector3 tmp = new Vector3d(cos(toRadians(angle)), sin(toRadians(angle)), 0);
        heading = heading.sub(tmp);
        heading = heading.normalize();
    }

    /**
     * Dreht die Turtle um einen bestimmten Winkel im Uhrzeigersinn.
     *
     * @param angle der Winkel in Grad
     */
    public void turnRight(double angle) {
        heading = heading.add(new Vector3d(cos(toRadians(angle)), sin(toRadians(angle)), 0));
        heading = heading.normalize();
        System.out.println("new heading: " + heading);
    }

    /**
     * Lässt die Turtle geradeaus gehen.
     *
     * @param length die Länge des Schritts
     */
    public void move(double length) {
        checkArgument(length > 0, "Schrittlänge kann nur positiv sein!");
        if (up) {
            position = position.add(heading.times(length));
        } else {
            Vector3 endPosition = position.add(heading.times(length));
            endPosition.setZ(-1);
            position.setZ(-1);
            Line line = new Line(position.toVertex3f(), endPosition.toVertex3f());
            turtleShape.add(line);
            position = endPosition;
        }
    }

    /**
     * Gibt die Blickrichtung der Turtle zurück.
     *
     * @return die Blickrichtung der Turtle
     */
    public Vector3 getHeading() {
        return heading;
    }

    /**
     * Gibt die Position der Turtle zurück.
     *
     * @return die Position der Turtle
     */
    public Vector3 getPosition() {
        return position;
    }

}
