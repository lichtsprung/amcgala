package org.amcgala.framework.raytracer;

/**
 * Ein Objekt, das das Interface Hittable implementiert, kann von einem RaytraceVisitor für Schnittpunktsberechnungen verwendet
 * werden.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public interface Hittable {

    /**
     * Berechnet den Schnittpunkt des Shapes mit dem Strahl, der vom Raytracer in die Scene geschickt wird.
     *
     * @param ray der Strahl, mit dem der Schnittpunkt berechnet werden soll
     *
     * @return das Ergebnis der Schnittpunktsberechnung
     */
    HitResult hit(Ray ray);
}
