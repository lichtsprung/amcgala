package org.amcgala.shape;

import java.util.List;

/**
 * Ein Polygonmesh, das aus einer beliebigen Anzahl von Polygonen bestehen kann.
 * Meshes können verwendet werden, um extern erstellte 3D Modelle in eine Szene zu importieren.
 * TODO Welche Datenstruktur ist hier am sinnvollsten?
 * - Liste von Dreiecken
 * - Ecken/Kantenliste
 */
public class Mesh extends AbstractShape {
    private List<Triangle> triangleList;

}
