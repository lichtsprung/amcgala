package org.amcgala.example.plotter;

import org.amcgala.Amcgala;
import org.amcgala.Scene;
import org.amcgala.framework.scenegraph.Node;

/**
 * User: Steffen Tröster
 * Date: 15.10.12
 * Time: 11:30
 * Hauptprogramm für den Plotter.
 */
public class Plotter extends Amcgala {

    private Scene scene = new Scene("PlotterScene");
    private Node graphNode = new Node("GraphNode");
    private Node graphNode2 = new Node("GraphNode2");

    /**
     * Graph ist abstrakt, daher müssen alle abstrakten Methoden implementiert werden.
     * Hier als Beispiel x^2
     */
    private Graph graph = new Graph(760, 200, "X-Achse", "Y-Achse") {
        @Override
        public double function(double x) {
            return Math.sin(x);
        }
    };
    /**
     * Graph ist abstrakt, daher müssen alle abstrakten Methoden implementiert werden.
     * Hier als Beispiel x^2
     */
    private Graph graph2 = new Graph(760, 200, "X-Achse", "Y-Achse") {
        @Override
        public double function(double x) {
            return Math.cos(x);
        }
    };

    public Plotter() {
        // Primitiver Line-Renderer wird gesetzt
        //this.scene.setRenderer(new BresenhamRenderer(scene.getCamera()));
        // fügt eine neue Scene hinzu
        this.framework.addScene(scene);
        // fügt ein neues Element mit Knotenpunkt zum Scenengraphen hinzu
        this.scene.add(graph, graphNode);
        this.scene.add(graph2, graphNode2);
        // startet das Framework
        this.framework.start();
    }

    public static void main(String[] args) {
        // Erstellt den Plotter und startet das Programm
        new Plotter();
    }
}
