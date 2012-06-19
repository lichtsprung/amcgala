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
package org.amcgala.framework.scenegraph.visitor;

import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Matrix;
import org.amcgala.framework.renderer.Renderer;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;

/**
 * Der RenderVisitor traversiert einmal pro Frame über den Szenengraph und
 * zeichnet jedes AbstractShape, das gefunden wird, auf den Canvas des Fensters.
 *
 * @author Robert Giacinto
 */
public class RenderVisitor implements Visitor {

    private static final Logger log = LoggerFactory.getLogger(RenderVisitor.class);
    private Renderer renderer;

    /**
     * Setzt den DefaultRenderer, der von diesem
     * <code>RenderVisitor</code> verwendet werden soll.
     *
     * @param renderer der neue DefaultRenderer
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Setzt die Kamera, die vom DefaultRenderer verwendet werden soll.
     *
     * @param camera die neue Kamera
     */
    public void setCamera(Camera camera) {
        if (renderer != null) {
            renderer.setCamera(camera);
        }
    }

    @Override
    public void visit(Node node) {
        synchronized (node.getShapes()) {
            Matrix transform = node.getTransformMatrix();
            renderer.setTransformationMatrix(transform);

            for (Shape shape : node.getShapes()) {
                try {
                    shape.render(renderer);
                } catch (ConcurrentModificationException ex) {
                    log.error("Caught an exception...", ex);
                }
            }
        }
    }
}
