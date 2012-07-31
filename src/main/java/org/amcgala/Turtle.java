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

    private CompositeShape turtleShape;

    private Vector3d heading = Vector3d.UNIT_Y;
    private Vector3d position = Vector3d.ZERO;
    private double headingAngle;
    private boolean up;


    public Turtle(CompositeShape shape) {

        turtleShape = shape;
        headingAngle = 90;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
    }

    public Turtle(Vector3d position, Vector3d heading, double headingAngle, CompositeShape shape) {
        this.position = position;
        this.heading = heading;
        this.turtleShape = shape;
        this.headingAngle = headingAngle;
    }


    public void up(){
        up = true;
    }

    public void down(){
        up = false;
    }

    public void turnLeft(double angle) {
        headingAngle += angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    public void turnRight(double angle) {
        headingAngle -= angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

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

    public Vector3d getHeading() {
        return heading;
    }

    public Vector3d getPosition() {
        return position;
    }

    public double getHeadingAngle() {
        return headingAngle;
    }
}
