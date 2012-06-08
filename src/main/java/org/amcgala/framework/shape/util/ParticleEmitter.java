package org.amcgala.framework.shape.util;

import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.transform.RotationZ;
import org.amcgala.framework.shape.BresenhamLine;
import org.amcgala.framework.shape.Shape;

import java.util.ArrayList;
import java.util.Random;

/**
 * Emitter Klasse die ParticleContainer mit den gegebenen Eigenschaften ausgibt.
 * Emitter kannt dis/enabled werden. TODO: Bis jetzt erst 2d moeglich !
 *
 * @author Steffen Troester
 */
public class ParticleEmitter extends Shape implements Updatable {
    private boolean enabled = true;
    private double width, height;
    private double x, y, z; // Mittepunkt
    private Vector3d direction = new Vector3d(0, 1, 0);
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    private ArrayList<ParticleManipulation> particleManipulations = new ArrayList<ParticleManipulation>();
    private RotationZ rectrotation = new RotationZ(Math.PI / 2);
    private boolean visible;
    // Emitting Settings
    private int timeIntervalMs = 100;
    private double particleSpeed = 1.0;
    // Temp Settings
    private long timeStamp;
    private Random r = new Random();

    @Override
    public void update() {
        if (timeStamp == 0 || !enabled) {
            setTimeStamp();
        } else {
            // Neuen Partikel anzeigen
            if (getTimeStampDifference() > timeIntervalMs) {
                setTimeStamp();
                // rotate direction
                Vector3d rot = rectrotation.getTransformMatrix()
                        .times(direction.copy().toMatrix()).toVector3d();
                // scale
                rot.times(r.nextDouble() * width);
                // translate
                rot.x += x;
                rot.y += y;
                // add
                particles.add(new Particle(particleSpeed, direction.copy(),
                        rot.x, rot.y));
            }
        }
        // Partikel updaten und gegebenenfalls manipulieren
        for (Particle p : particles) {
            p.update();
            for (ParticleManipulation pm : particleManipulations) {
                if (pm.fitInRange(p.getX(), p.getY())) {
                    pm.manipulate(p);
                }
            }
        }
        super.update();
    }

    public void setTimeStamp() {
        timeStamp = System.nanoTime() / 1000000;
    }

    public long getTimeStampDifference() {
        return (System.nanoTime() / 1000000) - timeStamp;
    }

    /**
     * 2D Constructor for Particle Emitter
     *
     * @param width
     * @param x
     * @param y
     * @param direction
     */
    public ParticleEmitter(double width, double x, double y, Vector3d direction) {
        this.width = width;
        this.x = x;
        this.y = y;
        this.direction = direction.normalize();
    }

    @Override
    public void render(Renderer renderer) {
        for (Particle p : particles) {
            p.render(renderer);
        }
        for (ParticleManipulation pm : particleManipulations) {
            pm.render(renderer);
        }
        // display emitterelement 2d
        if (isVisible()) {
            Vector3d scale = direction.copy().normalize().times(width);
            // rotate
            Vector3d rotateScale = rectrotation.getTransformMatrix()
                    .times(scale.toMatrix()).toVector3d();
            // translate
            rotateScale.x += x;
            rotateScale.y += y;
            // display
            new BresenhamLine(new Vector3d(x, y, 1), rotateScale).render(renderer);
        }
    }

    public void addParticleManipulation(ParticleManipulation p) {
        particleManipulations.add(p);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public Vector3d getScale() {
        Vector3d scale = direction.copy().times(width);
        return scale;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }


    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setTimeIntervalMs(int timeIntervalMs) {
        this.timeIntervalMs = timeIntervalMs;
    }

    public void setParticleSpeed(double particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
