package org.amcgala;


import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.Line;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.*;

/**
 * Die Klasse stellt Funktionlitäten zur Verfügung, die man benötigt, um Turtlegrafiken zu erstellen.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public abstract class TurtleMode {
    protected final static int WIDTH = 800;
    protected final static int HEIGHT = 600;
    private final Scene turtleScene = new Scene("turtle");
    private Vector3d heading = Vector3d.UNIT_X;
    private Vector3d position = Vector3d.ZERO;
    private double headingAngle;
    private boolean up;

    public TurtleMode() {
        turtleCommands();
        Framework framework = new Framework(WIDTH, HEIGHT);
        framework.add(turtleScene);
        framework.start();
    }

    private void set(int x, int y) {
        set((double) x, (double) y);
    }

    private void set(double x, double y) {
        position = new Vector3d(x, y, -1);
    }

    protected void up(){
        up = true;
    }

    protected void down(){
        up = false;
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
        checkArgument(length > 0, "Schrittlänge kann nur positiv sein!");

        if (up) {
            position = position.add(heading.times(length));
        } else {
            Vector3d endPosition = position.add(heading.times(length));
            endPosition.z = -1;
            position.z = -1;
            turtleScene.add(new Line(position, endPosition));
            position = endPosition;
        }
    }

    public abstract void turtleCommands();
}
