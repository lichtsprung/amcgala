package org.amcgala.framework.raytracer;

import org.amcgala.framework.math.Vector3d;

/**
 * Die ViewPlane des Raytracers ist die Projektionsfläche, durch die die Strahlen in die Szene geschickt werden.
 * Hierbei handelt es sich um wichtiges Hilfsmittel, um die Samplingdichte unabhängig von der Auflösung des darstellenden
 * Fensters zu halten.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class ViewPlane {
    private int verticalResolution;
    private int horizontalResolution;
    private double pixelSize;

    /**
     * Erstellt eine ViewPlane, die im Ursprung des Weltkoordinatensystems steht und in die negative z-Richtung blickt.
     *
     * @param verticalResolution   die vertikale Auflösung
     * @param horizontalResolution die horizontale Auflösung
     * @param pixelSize            die Größe eines Pixels der ViewPlane
     */
    public ViewPlane(int verticalResolution, int horizontalResolution, double pixelSize) {
        this.verticalResolution = verticalResolution;
        this.horizontalResolution = horizontalResolution;
        this.pixelSize = pixelSize;
    }

    /**
     * Gibt die Position eines Pixels der ViewPlane (column, row) in Weltkoordinaten zurück.
     *
     * @param column der Spaltenindex des Pixels
     * @param row    der Zeilenindex des Pixels
     *
     * @return die Position des Pixels in Weltkoordinaten als 3d Vektor
     */
    public Vector3d getWorldCoordinates(int column, int row) {
        double x = pixelSize * (column - 0.5 * (horizontalResolution - 1));
        double y = pixelSize * (row - 0.5 * (verticalResolution - 1));
        return new Vector3d(x, y, 100);
    }

    /**
     * Gibt die vertikale Auflösung der ViewPlane zurück.
     *
     * @return die vertikale Auflösung
     */
    public int getVerticalResolution() {
        return verticalResolution;
    }

    /**
     * Ändert die vertikale Auflösung der ViewPlane
     *
     * @param verticalResolution die vertikale Auflösung
     */
    public void setVerticalResolution(int verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    /**
     * Gibt die horizontale Auflösung der ViewPlane zurück.
     *
     * @return die horizontale Auflösung
     */
    public int getHorizontalResolution() {
        return horizontalResolution;
    }

    /**
     * Ändert die horizontale Auflösung der ViewPlane.
     *
     * @param horizontalResolution die horizontale Auflösung
     */
    public void setHorizontalResolution(int horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    /**
     * Gibt die Größe eines Pixels auf der ViewPlane zurück.
     *
     * @return die Pixelgröße
     */
    public double getPixelSize() {
        return pixelSize;
    }

    /**
     * Ändert die Größe eines Pixels der ViewPlane.
     *
     * @param pixelSize die neue Pixelgröße
     */
    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }
}
