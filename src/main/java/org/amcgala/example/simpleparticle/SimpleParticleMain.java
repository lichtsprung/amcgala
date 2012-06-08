package org.amcgala.example.simpleparticle;

import org.amcgala.Framework;
import org.amcgala.framework.math.Vector3d;
import org.amcgala.framework.shape.util.ParticleEmitter;
import org.amcgala.framework.shape.util.ParticleGravitation;

/**
 * Hier wird ein kleines Beispiel mit dem Partikelsystem gezeigt.
 *
 * @author Steffen Troester
 */
class SimpleParticleMain extends Framework {

    ParticleEmitter p;
    ParticleGravitation pg;

    public SimpleParticleMain(int width, int height) {
        super(width, height);
    }

    public static void main(String[] args) {
        new SimpleParticleMain(500, 500).start();
    }

    @Override
    public void initGraph() {
        // Partikelemmiter erstellen mit der laenge 20 und der Position
        // (100,100) und einer Richtung (schaeg)
        p = new ParticleEmitter(20, 100, 100, new Vector3d(-0.8, -0.4, 0));
        // 1.5 pixel pro frame
        p.setParticleSpeed(1.5);
        // alle 50 ms erscheint ein Partikel
        p.setTimeIntervalMs(50);
        // Zeige den Emitter an
        p.setVisible(true);
        // Erzeuge eine Gravitation bei (-150,-150) mit der Groesse 200x200
        pg = new ParticleGravitation(-150, -150, 200, 200);
        // Zeige auch diese Flaeche an
        pg.setVisible(true);
        // fuege die Gravitation dem Emmiter hinzu
        p.addParticleManipulation(pg);
        // Emmiter in den ScenenGraphen einbetten
        add(p);
    }
}