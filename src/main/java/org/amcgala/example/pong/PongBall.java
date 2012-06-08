/* 
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.amcgala.example.pong;

import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.shape.Shape;
import org.amcgala.framework.shape.shape2d.Circle;

/**
 * Der Ball, der vom Spieler getroffen werden muss.
 *
 * @author Robert Giacinto
 */
public class PongBall extends Shape {

    private double velocity;
    private Vector3d direction;
    private Vector3d position;
    private PongBoard board;
    private Circle circle;
    private Arrow arrow;

    /**
     * Ein Ball, der sich auf dem Pong Spielfeld bewegen kann, und von einem
     * Paddle abgewehrt werden kann.
     *
     * @param board das Board, auf dem sich der Ball bewegt
     */
    public PongBall(PongBoard board) {
        this.board = board;
        velocity = .11;
        direction = getRandomDirection().times(velocity);

        position = new Vector3d(board.getXmin() + board.getWidth() * board.getBallStart(), board.getHeight() / 2, -1);
        circle = new Circle(position.x, position.y, 5);
        arrow = new Arrow(position, direction, velocity * 50);
    }

    @Override
    public void update() {
        position.x += direction.x;
        position.y += direction.y;
        circle.setPosition(position.x, position.y);
        arrow.setPosition(position);
        checkPosition();
    }

    /**
     * Gibt die Bewegungsrichtung des Balls zurück.
     *
     * @return die aktuelle Bewegungsrichtung des Balls
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * Ändert die Bewegungsrichtung des Balls.
     *
     * @param direction die neue Richtung
     */
    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }

    /**
     * Gibt die aktuelle Geschwindigkeit des Balls zurück.
     *
     * @return die aktuelle Geschwindigkeit des Balls
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Ändert die Geschwindigkeit des Balls.
     *
     * @param velocity die Geschwindigkeit des Balls
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Gibt die Position des Balls zurück.
     *
     * @return die aktuelle Position des Balls
     */
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Ändert die Position des Balls.
     *
     * @param position die neue Position des Balls.
     */
    public void setPosition(Vector3d position) {
        this.position = position;
    }

    @Override
    public void render(Renderer renderer) {
        circle.render(renderer);
        arrow.render(renderer);
    }

    private Vector3d getRandomDirection() {
        return new Vector3d(Math.sin(Math.random() * Math.PI), Math.cos(Math.random() * Math.PI), -1).normalize();
    }

    private void checkPosition() {
        if (board.getBottom().isNearPlane(position) || board.getTop().isNearPlane(position)) {
            direction.y = -direction.y;
        } else if (board.getLeft().isNearPlane(position)) {
            direction.x = -direction.x;
        } else if (board.getXmax() < position.x) {
            position = new Vector3d(board.getXmin() + board.getWidth() * board.getBallStart(), board.getHeight() / 2, -1);
        }
    }
}
