package org.amcgala;


import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.Line;

import static java.lang.Math.*;

/**
 * Die Klasse stellt Funktionlitäten zur Verfügung, die man benötigt, um Turtlegrafiken zu erstellen.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public abstract class TurtleMode {
    private final Framework framework = new Framework(800, 600);
    private final Scene turtleScene = new Scene("turtle");
    private Vector3d heading = Vector3d.UNIT_X;
    private Vector3d position = Vector3d.ZERO;
    private double headingAngle;

    public TurtleMode() {
        turtleCommands();
        framework.addScene(turtleScene);
        framework.start();
    }

    protected void set(int x, int y) {
        set((double) x, (double) y);
    }

    protected void set(double x, double y) {
        position = new Vector3d(x, y, -1);
    }

    protected void turnLeft(double angle) {
        headingAngle += angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    protected void turnRight(double angle) {
        headingAngle -= angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    protected void move(double length) {
        Vector3d endPosition = position.add(heading.times(length));
        endPosition.z = -1;
        position.z = -1;
        turtleScene.add(new Line(position, endPosition));
        heading.normalize();
        position = endPosition;
    }

    public abstract void turtleCommands();
}
