package org.amcgala.raytracer.material;

import org.amcgala.math.MathConstants;
import org.amcgala.math.Vector3d;
import org.amcgala.raytracer.RGBColor;
import org.amcgala.raytracer.Ray;
import org.amcgala.raytracer.ShadingInfo;

/**
 * Spiegelndes Material für den Raytracer.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class MirrorMaterial extends Material {
    private float reflectionCoefficient;
    private RGBColor baseColor;

    /**
     * Ein reflektives Material, das die Umgebung spiegelt.
     *
     * @param reflectionCoefficient der Reflektionskoeffizient
     * @param baseColor             die Grundfarbe des Materials
     */
    public MirrorMaterial(float reflectionCoefficient, RGBColor baseColor) {
        this.reflectionCoefficient = reflectionCoefficient;
        this.baseColor = baseColor;
    }

    @Override
    public RGBColor getColor(ShadingInfo hit) {

        double angle = hit.normal.dot(hit.ray.direction.times(-1));
        Vector3d omegaI = hit.ray.direction.sub(hit.normal.times(-2 * angle));
        Ray refRay = new Ray(hit.hitPoint.travel(omegaI, MathConstants.EPSILON), omegaI);

        return baseColor.times(1 - reflectionCoefficient).add((hit.tracer.trace(refRay, hit.scene, hit.depth + 1).times(reflectionCoefficient)));
    }
}
