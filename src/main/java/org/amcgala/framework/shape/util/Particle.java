package org.amcgala.framework.shape.util;

import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.AbstractShape;
import org.amcgala.framework.shape.shape2d.Point2d;

/**
 * 2D - Partikel  für den {@link ParticleEmitter}.
 * TODO: Es sollte hier moeglich sein auch verschiedene Objekte als Particle zu nutzen !
 *
 * @author Steffen Tröster
 */
public class Particle extends AbstractShape implements Updatable {

    private double particleSpeed;
    private int life = 100;
    private Vector3d direction;

    private Point2d point2d;

    /**
     * Particle Constructor initialisiert vom ParticleEmitter
     *
     * @param particleSpeed Geschwindigkeit des Partikels
     * @param direction     die Bewegungsrichtung
     * @param x             x-Koordinate des Partikels
     * @param y             y-Koordinate des Partikels
     */
    public Particle(double particleSpeed, final Vector3d direction, double x,
                    double y) {
        this.particleSpeed = particleSpeed;
        this.direction = direction.normalize();

        this.point2d = new Point2d(x, y);
        Vector3d v = this.direction.copy();
        v.times(particleSpeed);
    }

    @Override
    public void update() {
        super.update();
        point2d.x += direction.x;
        point2d.y += direction.y;
        life--;
    }

    @Override
    public void render(Renderer renderer) {
        point2d.render(renderer);
    }

    /**
     * Gibt die Bewegungsrichtung des Partikels zurück.
     *
     * @return die Bewegungsrichtung
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * Ändert die Bewegungsrichtung des Partikels.
     *
     * @param direction die neue Richtung
     */
    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }


    /**
     * Gibt die aktuelle Geschwindigkeit des Partikels zurück.
     *
     * @return die Geschwindigkeit
     */
    public double getParticleSpeed() {
        return particleSpeed;
    }

    /**
     * Ändert die Geschwindigkeit des Partikels.
     *
     * @param particleSpeed die neue Geschwindigkeit
     */
    public void setParticleSpeed(double particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    /**
     * Gibt die x-Koordinate der Position des Partikels zurück.
     *
     * @return die x-Koordinate der Position
     */
    public double getX() {
        return point2d.x;
    }

    /**
     * Gibt die y-Koordinate des Partikels zurück.
     * @return die y-Koordinate der Position
     */
    public double getY() {
        return point2d.y;
    }

    /**
     * Gibt die Lebenskraft des Partikels zurück.
     * @return die Lebenskraft
     */
    public int getLife() {
        return life;
    }
}
