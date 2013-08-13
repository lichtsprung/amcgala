package org.amcgala.shape.util.lsystem;

import org.amcgala.Turtle;
import org.amcgala.TurtleState;
import org.amcgala.math.Vector3d;
import org.amcgala.shape.util.CompositeShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger log = LoggerFactory.getLogger(LindenmayerSystem.class);
    private Axiom axiom;
    private Rules rules;
    private Level level;
    private Stack<Turtle> turtles;
    private Turtle turtle;
    private CompositeShape shape;
    private String current;
    private Length length;
    private Angle angle;


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
    public LindenmayerSystem(Axiom axiom, Rules rules, Level level, Length length, Angle angle, CompositeShape shape, Vector3d startPosition, float startHeading) {
        this(axiom, rules, level, length, angle, shape);
        Vector3d heading = new Vector3d(cos(toRadians(startHeading)), -sin(toRadians(startHeading)), 0).normalize();
        turtle = new Turtle(new TurtleState(startHeading, heading, startPosition), shape);
    }


    /**
     * Wendet die Regeln des L-Systems auf das Startsymbol an.
     */
    public void run() {
        applyRules();
        for (char c : current.toCharArray()) {
            switch (c) {
                case '+':
                    log.debug("Right Turn: {}", angle.value);
                    turtle.turnRight(angle.value);
                    break;
                case '-':
                    log.debug("Left Turn: {}", angle.value);
                    turtle.turnLeft(angle.value);
                    break;
                case '[':
                    log.debug("Push Turtle to stack");
                    Turtle t = new Turtle(turtle.getTurtleState(), shape);
                    turtles.push(turtle);
                    turtle = t;
                    break;
                case ']':
                    log.debug("Pop Turtle from stack");
                    turtle = turtles.pop();
                    break;
                case 'm':
                    log.debug("Move Turtle: {}", length.value);
                    turtle.up();
                    turtle.move(length.value);
                    turtle.down();
                    break;
                case 'M':
                    log.debug("Draw line: {}", length.value);
                    turtle.move(length.value);
                    break;
                default:
                    break;
            }
        }
    }

    /*
    * Hier passiert was
    */
    private void applyRules() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level.level; i++) {
            for (char c : current.toCharArray()) {
                sb.append(rules.applyReplacementRules(Character.toString(c)));
            }
            current = sb.toString();
            sb = new StringBuilder();
        }
        for (char c : current.toCharArray()) {
            sb.append(rules.applyDrawingRules(Character.toString(c)));
        }

        current = sb.toString();
    }
}
