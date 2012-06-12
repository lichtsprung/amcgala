package org.amcgala.example.extendedparticle;

import com.google.common.eventbus.Subscribe;
import org.amcgala.framework.animation.Updatable;
import org.amcgala.framework.event.InputHandler;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.transform.RotationZ;
import org.amcgala.framework.shape.Shape;
import org.amcgala.framework.shape.shape2d.Rectangle;
import org.amcgala.framework.shape.util.ParticleEmitter;
import org.amcgala.framework.shape.util.ParticleGravitation;

import java.awt.event.KeyEvent;

public class ParticleShip extends Shape implements InputHandler, Updatable {

    private ParticleEmitter pe = new ParticleEmitter(20, 0, 0, new Vector3d(0, -1, 0));
    private ParticleGravitation pg = new ParticleGravitation(-250, -250, 500, 500);

    private Vector3d direction = Vector3d.ZERO.copy();
    private Vector3d rotation = Vector3d.UNIT_Y.copy();
    private Rectangle rectangle = new Rectangle(0, 0, 20, 30);
    private double speed;

    public ParticleShip() {
        pe.setParticleSpeed(1.5);
        pe.setTimeIntervalMs(20);
        pe.setEnabled(false);
        pe.addParticleManipulation(pg);
        pe.setVisible(true);
        pg.setGravitation(0.05);
        this.speed = 0.1;
        pg.setVisible(true);
    }

    @Override
    public void render(Renderer renderer) {
        pe.render(renderer);
        rectangle.render(renderer);
    }

    @Override
    public void update() {
        pe.update();

        double gravitation = pg.getGravitation();

        pe.setY(pe.getY() + direction.y);
        pe.setX(pe.getX() + direction.x);

        direction.y -= gravitation;

        if (pe.isEnabled()) {
            direction.x += speed * rotation.x;
            direction.y += speed * rotation.y;
        }

        rectangle.top.x1 += direction.x;
        rectangle.top.x2 += direction.x;
        rectangle.top.y1 += direction.y;
        rectangle.top.y2 += direction.y;

        rectangle.left.x1 += direction.x;
        rectangle.left.x2 += direction.x;
        rectangle.left.y1 += direction.y;
        rectangle.left.y2 += direction.y;

        rectangle.right.x1 += direction.x;
        rectangle.right.x2 += direction.x;
        rectangle.right.y1 += direction.y;
        rectangle.right.y2 += direction.y;

        rectangle.bottom.x1 += direction.x;
        rectangle.bottom.x2 += direction.x;
        rectangle.bottom.y1 += direction.y;
        rectangle.bottom.y2 += direction.y;


        super.update();
    }

    @Subscribe
    public void shipNavigation(KeyEvent e) {
        RotationZ rotationZ;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    pe.setEnabled(true);
                } else {
                    pe.setEnabled(false);
                }
                break;
            case KeyEvent.VK_RIGHT:
                rotationZ = new RotationZ(-0.02);
                rotation = rotationZ.getTransformMatrix()
                        .times(rotation.toMatrix()).toVector3d();

                rotationZ = new RotationZ(Math.PI);
                pe.setDirection(rotationZ.getTransformMatrix().times(rotation.copy().toMatrix()).toVector3d());
                break;
            case KeyEvent.VK_LEFT:
                rotationZ = new RotationZ(0.02);
                rotation = rotationZ.getTransformMatrix()
                        .times(rotation.toMatrix()).toVector3d();
                rotationZ = new RotationZ(Math.PI);
                pe.setDirection(rotationZ.getTransformMatrix().times(rotation.copy().toMatrix()).toVector3d());
                break;
        }
    }

    public Vector3d getDirection() {
        return direction;
    }

    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }
}
