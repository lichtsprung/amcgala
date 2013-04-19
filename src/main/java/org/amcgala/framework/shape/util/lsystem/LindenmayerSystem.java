package org.amcgala.framework.shape.util.lsystem;

import org.amcgala.Turtle;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.util.CompositeShape;

import java.util.Stack;

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
                    Vector3d position = turtle.getPosition();
                    Vector3d heading = turtle.getHeading();
                    double headingAngle = turtle.getHeadingAngle();
                    Turtle t = new Turtle(position, heading, headingAngle, shape);
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
