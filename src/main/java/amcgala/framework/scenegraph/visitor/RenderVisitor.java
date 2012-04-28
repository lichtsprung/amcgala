/* 
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package amcgala.framework.scenegraph.visitor;

import amcgala.framework.camera.Camera;
import amcgala.framework.lighting.Light;
import amcgala.framework.math.Matrix;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.scenegraph.Node;
import amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Der RenderVisitor traversiert einmal pro Frame über den Szenengraph und
 * zeichnet jedes Shape, das gefunden wird, auf den Canvas des Fensters.
 *
 * @author Robert Giacinto
 */
public class RenderVisitor implements Visitor {

    private static final Logger log = LoggerFactory.getLogger(RenderVisitor.class);
    private Renderer renderer;
    private Camera camera;

    /**
     * Setzt den Renderer, der von diesem
     * <code>RenderVisitor</code> verwendet werden soll.
     *
     * @param renderer der neue Renderer
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Setzt die Kamera, die vom Renderer verwendet werden soll.
     *
     * @param camera die neue Kamera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void visit(Node node) {
        synchronized (node.getGeometry()) {
            Matrix transform = node.getTransformMatrix();
            Collection<Light> lights = node.getLights();
            for (Shape shape : node.getGeometry()) {
                shape.setRendering(true);
                try {
                	// licht muss hier mit übergeben werden!
                    shape.render(transform, camera, renderer, lights);
                } catch (ConcurrentModificationException ex) {
                    // Ignore exception since we don't care for thread-safety at this point.
                    log.info("Caught an  exception...");
                }
                shape.setRendering(false);
            }
        }
    }
}
