package org.amcgala.framework.renderer;

import org.amcgala.framework.shape.primitives.LinePrimitive;
import org.amcgala.framework.shape.primitives.PointPrimitive;
import org.amcgala.framework.shape.primitives.QuadPrimitive;
import org.amcgala.framework.shape.primitives.TrianglePrimitive;

import java.util.List;

/**
 * DisplayList, die vom Renderer verwendet wird.
 */
public class DisplayList {
    public List<LinePrimitive> lines;
    public List<QuadPrimitive> quads;
    public List<TrianglePrimitive> triangles;
    public List<PointPrimitive> points;


    public void add(DisplayList displayList) {
        lines.addAll(displayList.lines);
        quads.addAll(displayList.quads);
        triangles.addAll(displayList.triangles);
        points.addAll(displayList.points);
    }
}
