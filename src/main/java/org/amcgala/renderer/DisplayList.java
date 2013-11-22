package org.amcgala.renderer;

import com.google.common.base.Objects;
import org.amcgala.shape.primitives.LinePrimitive;
import org.amcgala.shape.primitives.PointPrimitive;
import org.amcgala.shape.primitives.RectanglePrimitive;
import org.amcgala.shape.primitives.TrianglePrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * DisplayList, die vom Renderer verwendet wird.
 */
public class DisplayList {
    public List<LinePrimitive> lines = new ArrayList<>();
    public List<TrianglePrimitive> triangles = new ArrayList<>();
    public List<PointPrimitive> points = new ArrayList<>();
    public List<RectanglePrimitive> rects = new ArrayList<>();

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
}
