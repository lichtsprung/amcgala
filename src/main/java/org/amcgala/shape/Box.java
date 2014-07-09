package org.amcgala.shape;

import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.scenegraph.transform.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Box
 */
public class Box extends AbstractShape {

    private Vertex3f position;

    private float width;
    private float height;
    private float depth;

    private List<Line> lines;

    public Box(Vertex3f position, float width, float height, float depth) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.depth = depth;
        lines = new ArrayList<>(12);

        lines.add(new Line(position, new Vertex3f(position.x + width, position.y, position.z)));
        lines.add(new Line(position, new Vertex3f(position.x, position.y + height, position.z)));
        lines.add(new Line(new Vertex3f(position.x + width, position.y, position.z), new Vertex3f(position.x + width, position.y + height, position.z)));
        lines.add(new Line(new Vertex3f(position.x, position.y + height, position.z), new Vertex3f(position.x + width, position.y + height, position.z)));

        lines.add(new Line(position, new Vertex3f(position.x, position.y, position.z + depth)));
        lines.add(new Line(new Vertex3f(position.x, position.y + height, position.z), new Vertex3f(position.x, position.y + height, position.z + depth)));
        lines.add(new Line(new Vertex3f(position.x, position.y, position.z + depth), new Vertex3f(position.x, position.y + height, position.z + depth)));

        lines.add(new Line(new Vertex3f(position.x + width, position.y, position.z), new Vertex3f(position.x + width, position.y, position.z + depth)));
        lines.add(new Line(new Vertex3f(position.x + width, position.y + height, position.z), new Vertex3f(position.x + width, position.y + height, position.z + depth)));
        lines.add(new Line(new Vertex3f(position.x + width, position.y, position.z + depth), new Vertex3f(position.x + width, position.y + height, position.z + depth)));

        lines.add(new Line(new Vertex3f(position.x, position.y, position.z+depth), new Vertex3f(position.x + width, position.y, position.z + depth)));
        lines.add(new Line(new Vertex3f(position.x, position.y+height, position.z + depth), new Vertex3f(position.x +width, position.y + height, position.z + depth)));
    }

    @Override
    public DisplayList getDisplayList(DisplayList list) {
        DisplayList dl = list;
        for (Line l : lines) {
            dl = l.getDisplayList(dl);
        }
        return dl;
    }

    @Override
    public DisplayList getDisplayList(DisplayList list, Transformation transformation) {
        // TODO Füge die Primitiven der DisplayList hinzu, aber vorher transformiere die Vektoren mithilfe des Transformationsobjekts.
        return super.getDisplayList(list, transformation);
    }
}
