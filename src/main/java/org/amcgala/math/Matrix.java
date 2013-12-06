/*
* Code von http://math.nist.gov/javanumerics/jama/.
* Keine Lizenz, Code wurde der Public Domain überlassen.
*/
package org.amcgala.math;

import org.ejml.simple.SimpleMatrix;

/**
 *
 */
public class Matrix {

    private SimpleMatrix m;

    public Matrix(int rows, int columns, double... data) {
        m = new SimpleMatrix(rows, columns, true, data);
    }

    public Matrix(double[][] values) {
        m = new SimpleMatrix(values);
    }

    private Matrix(SimpleMatrix m) {
        this.m = m;
    }


    public float get(int r, int c) {
        return (float) m.get(r, c);
    }


    public Matrix times(Matrix that) {

        return new Matrix(m.mult(that.m));
    }

    public static Matrix identity(int rows, int columns) {
        return new Matrix(SimpleMatrix.identity(rows));
    }

    public static Matrix fromFrustrum(double left, double right, double bottom, double top, double near, double far) {
        double x = 2 * near / (right - left);
        double y = 2 * near / (top - bottom);
        double a = (right + left) / (right - left);
        double b = (top + bottom) / (top - bottom);
        double c = -(far + near) / (far - near);
        double d = -2 * far * near / (far - near);

        double[][] values = {
                {x, 0, a, 0},
                {0, y, b, 0},
                {0, 0, c, d},
                {0, 0, -1, 0}
        };

        return new Matrix(values);
    }

    public static Matrix getPerspective(double fieldOfView, double aspect, double near, double far) {
        double ymax = near * Math.tan(fieldOfView * Math.PI / 360);
        double ymin = -ymax;
        double xmin = ymin * aspect;
        double xmax = ymax * aspect;


        return fromFrustrum(xmin, xmax, ymin, ymax, near, far);
    }


    public static Matrix lookAt(Vector3d eye, Vector3d target, Vector3d up) {
        Vector3d z = eye.sub(target).normalize();
        if (z.length() == 0) {
            z.setZ(1);
        }

        Vector3d x = up.cross(z).normalize();
        if (x.length() == 0) {
            z.setX(z.getX() + 0.0001);
            x = up.cross(z).normalize();
        }

        Vector3d y = z.cross(x);

        double[][] values = {
                {x.getX(), y.getX(), z.getX()},
                {x.getY(), y.getY(), z.getY()},
                {x.getZ(), y.getZ(), z.getZ()},
                {0, 0, 0, 1}
        };

        return new Matrix(values);
    }

    @Override
    public String toString() {
        return m.toString();
    }
}
