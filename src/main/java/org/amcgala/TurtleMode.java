package org.amcgala;


import org.amcgala.framework.math.Vector3;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.math.Vertex3f;
import org.amcgala.framework.math.util.Vectors;
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
    /**
     * Die Standardbreite des Turtlefensters.
     */
    protected final static int WIDTH = 800;
    /**
     * Die Standardhöhe des Turtlefensters.
     */
    protected final static int HEIGHT = 600;

    // Die Szene, in der der Turtle-Modus aktiv ist.
    private final Scene scene = new Scene("turtle");

    // Die Blickrichtung der Turtle. Sie schaut in Richtung der x-Achse. Also nach rechts.
    private Vector3 heading = new Vector3d(1, 0, 0);

    // Die Turtle steht im Urspung des Koordinatensystems. Aktuell in der Mitte des Fensters.
    private Vector3 position = new Vector3d(0, 0, 0);

    // Der Blinkwinkel - das gleiche wie heading, nur dass es sich hierbei um eine Graddarstellung im Bogenmaß handelt.
    private double headingAngle;

    // Wenn up true ist, dann zeichnet die Turtle nicht.
    private boolean up;


    /**
     * Erzeugt ein TurtleMode Fenster der Größe 800x600.
     */
    public TurtleMode() {
        turtleCommands();
        Framework framework = Framework.createInstance(WIDTH, HEIGHT);
        framework.addScene(scene);
    }

    /**
     * Erzeugt ein TurtleMode Fenster einer beliebigen Größe.
     *
     * @param width  die Breite des Fensters
     * @param height die Höhe des Fensters
     */
    public TurtleMode(int width, int height) {
        turtleCommands();
        Framework framework = Framework.createInstance(width, height);
        framework.addScene(scene);
    }

    private void set(int x, int y) {
        set(x, y);
    }

    private void set(float x, float y) {
        position = new Vector3d(x, y, -1);
    }

    /**
     * Nimmt den Zeichenstift der Turtle von der Zeichenfläche und stoppt das Zeichnen.
     * Nach dem Aufruf dieser Methode kann die Turtle bewegt werden, ohne Änderungen an der Zeichnung vorzunehmen.
     */
    protected void up() {
        up = true;
    }

    /**
     * Setzt den Zeichenstift der Turtle wieder auf der Zeichenfläche ab und beginnt das Zeichnen, sobald die Turtle nach
     * dem Aufruf dieser Methode wieder bewegt wird.
     */
    protected void down() {
        up = false;
    }

    /**
     * Dreht die Turtle entgegen den Uhrzeigersinn um den Winkel, der als Parameter übergeben wird.
     *
     * @param angle der Winkel in Grad
     */
    protected void turnLeft(float angle) {
        headingAngle -= angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    /**
     * Dreht die Turtle im Uhrzeigersinn um den Winkel, der als Parameter übergeben wird.
     *
     * @param angle der Winkel in Grad
     */
    protected void turnRight(float angle) {
        headingAngle += angle;
        heading = new Vector3d(cos(toRadians(headingAngle)), sin(toRadians(headingAngle)), -1);
        heading.normalize();
    }

    /**
     * Bewegt die Turtle um eine bestimmte Länge in Blickrichtung der Turtle.
     *
     * @param length die Länge des Schritts
     */
    protected void move(double length) {
//  TODO muss korrigiert werden, sobald GL funktioniert.
        checkArgument(length > 0, "Schrittlänge kann nur positiv sein!");

        if (up) {
            position = position.add(heading.times(length));
        } else {
            Vertex3f endPosition = Vectors.toVertex3f(position.add(heading.times(length)));
            Vertex3f startPosition = Vectors.toVertex3f(position);
            endPosition.z = -1;
            startPosition.z = -1;
            scene.addShape(new Line(startPosition, endPosition));
            position = Vectors.toVector3d(endPosition);
        }
    }

    public abstract void turtleCommands();
}
