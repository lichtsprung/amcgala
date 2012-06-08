package org.amcgala.framework.shape.util;

import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;
import org.amcgala.framework.shape.shape2d.Point2d;

/**
 * Partikel Element fuer den ParticleEmitter. TODO: Es sollte hier moeglich sein
 * auch verschiedene Objekte als Particle zu nutzen !
 *
 * @author Steffen Troester
 */
public class Particle extends Shape implements Updatable {

    private double particleSpeed;
    private Vector3d direction;

    private Point2d point2d;

    /**
     * Particle Constructor initialisiert vom ParticleEmitter
     *
     * @param particleSpeed
     * @param direction
     * @param x
     * @param y
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
        point2d.x += direction.x;
        point2d.y += direction.y;
        super.update();
    }

    @Override
    public void render(Renderer renderer) {
        point2d.render(renderer);
    }

    public Vector3d getDirection() {
        return direction;
    }

    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }

    public double getParticleSpeed() {
        return particleSpeed;
    }

    public void setParticleSpeed(double particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    public double getX() {
        return point2d.x;
    }

    public double getY() {
        return point2d.y;
    }

}
