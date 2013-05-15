package org.amcgala.framework.shape.util.lsystem;

import org.amcgala.Turtle;
import org.amcgala.framework.math.Vector3;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.util.CompositeShape;

import java.util.Stack;

import static java.lang.Math.*;

/**
 * Ein Lindenmayer-System, das folgende Zeichnen interpretieren kann:
 * - + : Rechtsdrehung
 * - - : Linksdrehung
 * - [ : Push
 * - ] : Pop
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class LindenmayerSystem {
    private Axiom axiom;
    private Rules rules;
    private Level level;
    private Stack<Turtle> turtles;
    private Turtle turtle;
    private CompositeShape shape;
    private String current;
    private Length length;
    private Angle angle;
    private Vector3 startPosition;


    /**
     * Erstellt ein neues L-System.
     *
     * @param axiom  das Startsymbol des L-Systems
     * @param rules  die Ersetzungsregeln, die auf das Axiom angewendet werden
     * @param level  die Rekusionstiefe
     * @param length die Schrittweite
     * @param angle  der Winkel, um den gedreht wird
     * @param shape  das Shape, das für die Darstellung des L-Systems verwendet werden soll
     */
    public LindenmayerSystem(Axiom axiom, Rules rules, Level level, Length length, Angle angle, CompositeShape shape) {
        this.axiom = axiom;
        this.rules = rules;
        this.level = level;
        this.length = length;
        this.angle = angle;
        this.shape = shape;
        turtles = new Stack<Turtle>();
        turtle = new Turtle(shape);
        current = axiom.axiom;
    }

    /**
     * Erstellt ein neues L-System.
     *
     * @param axiom         das Startsymbol des L-Systems
     * @param rules         die Ersetzungsregeln, die auf das Axiom angewendet werden
     * @param level         die Rekusionstiefe
     * @param length        die Schrittweite
     * @param angle         der Winkel, um den gedreht wird
     * @param shape         das Shape, das für die Darstellung des L-Systems verwendet werden soll
     * @param startPosition die Startposition der Turtle
     */
    public LindenmayerSystem(Axiom axiom, Rules rules, Level level, Length length, Angle angle, CompositeShape shape, Vector3 startPosition, float startHeading) {
        this(axiom, rules, level, length, angle, shape);
        this.startPosition = startPosition;
        turtle = new Turtle(startPosition, new Vector3d(cos(toRadians(startHeading)), -sin(toRadians(startHeading)), -1), startHeading, shape);
    }


    /**
     * Wendet die Regeln des L-Systems auf das Startsymbol an.
     */
    public void run() {
        applyRules();
        for (char c : current.toCharArray()) {
            switch (c) {
                case '+':
                    turtle.turnRight(angle.angle);
                    break;
                case '-':
                    turtle.turnLeft(angle.angle);
                    break;
                case '[':
                    Vector3 position = turtle.getPosition();
                    Vector3 heading = turtle.getHeading();
                    Turtle t = new Turtle(position, heading, turtle.getHeadingAngle(), shape);
                    turtles.push(turtle);
                    turtle = t;
                    break;
                case ']':
                    turtle = turtles.pop();
                    break;
                case 'm':
                    turtle.up();
                    turtle.move(length.length);
                    turtle.down();
                    break;
                case 'M':
                    turtle.move(length.length);
                    break;
                default:
                    break;
            }
        }
    }

    private void applyRules() {
        StringBuilder sb = new StringBuilder(100);
        for (int i = 0; i < level.level; i++) {
            for (char c : current.toCharArray()) {
                sb.append(rules.applyReplacementRules(Character.toString(c)));
            }
            current = sb.toString();
            sb = new StringBuilder(100);
        }

        for (char c : current.toCharArray()) {
            sb.append(rules.applyDrawingRules(Character.toString(c)));
        }
        current = sb.toString();
    }
}
