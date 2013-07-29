package org.amcgala.math.util;

import org.amcgala.math.Vector3;
import org.amcgala.math.Vector3d;
import org.amcgala.math.Vertex3f;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vectors {

    public static Vertex3f toVertex3f(Vector3 vector) {
        return new Vertex3f((float) vector.getX(), (float) vector.getY(), (float) vector.getZ());
    }

    public static Vector3d toVector3d(Vertex3f vertex3f) {
        return new Vector3d(vertex3f.x, vertex3f.y, vertex3f.z);
    }
}
