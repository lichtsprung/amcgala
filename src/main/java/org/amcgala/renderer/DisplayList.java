package org.amcgala.renderer;

import com.google.common.base.Objects;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.PointPrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * DisplayList, die vom Renderer verwendet wird.
 */
public class DisplayList {
    public List<LinePrimitive> lines = new ArrayList<LinePrimitive>();
    public List<TrianglePrimitive> triangles = new ArrayList<TrianglePrimitive>();
    public List<PointPrimitive> points = new ArrayList<PointPrimitive>();


    public void add(DisplayList displayList) {
        lines.addAll(displayList.lines);
        triangles.addAll(displayList.triangles);
        points.addAll(displayList.points);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("lines", lines).add("triangles", triangles).add("points", points).toString();
    }
}
