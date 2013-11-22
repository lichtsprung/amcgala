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
package org.amcgala.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repräsentation von 3D-Vektoren.
 */
public class Vector3d {
    private static final Logger log = LoggerFactory.getLogger(Vector3d.class);
    public static final Vector3d UNIT_X = createVector3d(1, 0, 0);
    public static final Vector3d UNIT_Y = createVector3d(0, 1, 0);
    public static final Vector3d UNIT_Z = createVector3d(0, 0, 1);
    public static final Vector3d ZERO = createVector3d(0, 0, 0);
    public double x, y, z;

    /**
     * Gibt eine neue Instanz eines Vektors zurück.
     *
     * @param x die x-Komponente des neuen Vektors
     * @param y die y-Komponente des neuen Vektors
     * @param z die z-Komponente des neuen Vektors
     * @return einen neuen Vektor (x, y, z)
     */
    public static Vector3d createVector3d(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    /**
     * Gibt eine Kopie des übergebenen Vektors zurück.
     *
     * @param v der zu kopierende Vektor
     * @return die Kopie des Vektors
     */
    public static Vector3d createVector3d(Vector3d v) {
        return new Vector3d(v);
    }

    @Override
    public String toString() {
        return "Vector3d{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }


    /**
     * Erzeugt einen neuen 3D-Vektor.
     *
     * @param x x-Komponente
     * @param y y-Komponente
     * @param z z-Komponente
     */
    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy-Konstruktor.
     *
     * @param v Vektor, der kopiert werden soll
     */
    public Vector3d(Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Skalarprodukt mit einem anderen Vektor
     *
     * @param that der andere Vektor
     * @return Skalarprodukt
     */
    public double dot(Vector3d that) {
        return this.x * that.getX() + this.y * that.getY() + this.z * that.getZ();
    }


    /**
     * Kreuzprodukt zweier Vektoren
     *
     * @param that der andere Vektor
     * @return Ergebnisvektor
     */
    public Vector3d cross(Vector3d that) {
        return createVector3d(
                this.y * that.getZ() - this.z * that.getY(),
                this.z * that.getX() - this.x * that.getZ(),
                this.x * that.getY() - this.y * that.getX());
    }

    /**
     * Subtrahiert einen Vektor von diesem Vektor.
     *
     * @param that der andere Vektor
     * @return Ergebnisvektor
     */
    public Vector3d sub(Vector3d that) {
        return createVector3d(
                this.x - that.getX(),
                this.y - that.getY(),
                this.z - that.getZ());
    }

    /**
     * Addiert einen Vektor zu diesem Vektor
     *
     * @param that der andere Vektor
     * @return Ergebnisvektor
     */
    public Vector3d add(Vector3d that) {
        return createVector3d(
                this.x + that.getX(),
                this.y + that.getY(),
                this.z + that.getZ());
    }

    /**
     * Die Länge des Vektors
     *
     * @return die Länge
     */
    public double length() {
        return Math.sqrt(dot(this));
    }

    /**
     * Die quadrierte Länge des Vektors
     *
     * @return quadrierte Länge
     */
    public double lengthSquared() {
        return dot(this);
    }

    /**
     * Multipliziert diesen Vektor mit einem Skalar
     *
     * @param s die skalare Größe
     * @return Ergebnisvektor
     */
    public Vector3d times(double s) {
        return createVector3d(x * s, y * s, z * s);
    }

    /**
     * Gibt den Vektor normalisiert zurück.
     *
     * @return der normalisierte Vektor
     */
    public Vector3d normalize() {
        double norm = 1.0 / length();
        return createVector3d(x * norm, y * norm, z * norm);
    }

    /**
     * Gibt den Vektor in einer 4d Matrix zurück. Diese wird für viele Transformationen benötigt.
     *
     * @return die Matrixrepräsentation des Vektors
     */
    public Matrix toMatrix() {
        double[] vals = {x, y, z, 1};
        return new Matrix(4, 1, vals);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Transformiert den Vektor mithilfe einer Transformationsmatrix und gibt eine Kopie des transformierten Vektors zurück.
     *
     * @param transformation die Transformationsmatrix
     * @return der transformierte Vektor
     */
    public Vector3d transform(Matrix transformation) {
        Matrix tmp = transformation.times(toMatrix());
        return createVector3d(tmp.get(0, 0), tmp.get(1, 0), tmp.get(2, 0));
    }

    /**
     * Bewegt den Vektor entlang eines Richtungsvektors um den Faktorwert t.
     *
     * @param direction die Richtung
     * @param t         der Faktor
     * @return der neue Vektor v_neu = v_alt + direction * t
     */
    public Vector3d travel(Vector3d direction, double t) {
        return new Vector3d(x + direction.getX() * t, y + direction.getY() * t, z + direction.getZ() * t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3d vector3d = (Vector3d) o;

        return Double.compare(vector3d.x, x) == 0 && Double.compare(vector3d.y, y) == 0 && Double.compare(vector3d.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = z != +0.0d ? Double.doubleToLongBits(z) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Vector3d copy() {
        return new Vector3d(x, y, z);
    }

    public Vertex3f toVertex3f() {
        return new Vertex3f((float) x, (float) y, (float) z);
    }

    public int compareTo(Vector3d that) {
        double length = that.sub(this).length();
        if (length > 0) {
            return 1;
        } else if (length < 0) {
            return -1;
        }
        return 0;
    }
}
