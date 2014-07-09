package org.amcgala.renderer;

import com.google.common.base.Objects;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.PointPrimitive;
import org.amcgala.shape.primitives.RectanglePrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DisplayList, die vom Renderer verwendet wird.
 */
public class DisplayList {
    public Set<LinePrimitive> lines = new HashSet<>();
    public Set<TrianglePrimitive> triangles = new HashSet<>();
    public Set<PointPrimitive> points = new HashSet<>();
    public Set<RectanglePrimitive> rects = new HashSet<>();

    public void clear(){
        lines.clear();
        triangles.clear();
        points.clear();
        rects.clear();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()).add("lines", lines).add("triangles", triangles).add("points", points).toString();
    }

    public void addAll(DisplayList displayList) {
        lines.addAll(displayList.lines);
        triangles.addAll(displayList.triangles);
        rects.addAll(displayList.rects);
        points.addAll(displayList.points);
    }
}
