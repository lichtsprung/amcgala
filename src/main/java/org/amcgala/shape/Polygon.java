package org.amcgala.shape;

import com.google.common.base.Preconditions;
import org.amcgala.math.Vector3d;
import org.amcgala.renderer.DisplayList;
import org.amcgala.scenegraph.transform.Transformation;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasse, die Polygon darstellen kann. Es können eine beliebige Anzahl von Ecken angegeben werden. Es wird eine Tria
 */
public class Polygon extends AbstractShape {
    private List<Vector3d> points = new ArrayList<Vector3d>();
    private boolean fill;

    /**
     * Ein Polygon, das über die Punkte definiert ist, die im Konstruktor übergeben werden.
     *
     * @param points die Eckpunkte des Polygons
     */
    public Polygon(Vector3d... points) {
        Collections.addAll(this.points, points);
    }

    /**
     * Fügt dem Polygon einen neuen Punkt am Ende der Liste hinzu.
     *
     * @param point der neue Eckpunkt
     */
    public void addPoint(Vector3d point) {
        points.add(point);
    }

    /**
     * Entfernt einen Eckpunkt aus dem Polygon.
     *
     * @param index der Index des Punktes, der entfernt werden soll
     */
    public void removePoint(int index) {
        Preconditions.checkElementIndex(index, points.size());
        points.remove(index);
    }

    /**
     * Berechnet die Triangulation des Polygons.
     *
     * @return Ergebnis der Triangulation
     */
    protected List<Triangle> tesselatePolygon() {
        // TODO Triangulation implementieren
        throw new NotImplementedException();
    }

    /**
     * Ob das Polygon gefüllt oder im Wireframe-Modus gezeichnet werden soll.
     *
     * @param fill true, wenn Polygon gefüllt werden soll
     */
    public void fill(boolean fill) {
        this.fill = fill;
    }


    @Override
    public DisplayList getDisplayList(DisplayList list) {
        List<Triangle> tesselation = tesselatePolygon();
        for (Triangle tri : tesselation) {
            tri.getDisplayList(list);
        }
        return list;
    }

    @Override
    public DisplayList getDisplayList(DisplayList list, Transformation transformation) {
        // TODO Füge die Primitiven der DisplayList hinzu, aber vorher transformiere die Vektoren mithilfe des Transformationsobjekts.
        return super.getDisplayList(list, transformation);
    }
}