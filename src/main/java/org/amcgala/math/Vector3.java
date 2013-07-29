package org.amcgala.math;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Vector3 extends Comparable<Vector3> {
    double dot(Vector3 that);

    Vector3 cross(Vector3 that);

    Vector3 sub(Vector3 that);

    Vector3 add(Vector3 that);

    double length();

    double lengthSquared();

    Vector3 times(double s);

    Vector3 normalize();

    Matrix toMatrix();

    double getX();

    double getY();

    double getZ();

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    public Vertex3f toVertex3f();
}
