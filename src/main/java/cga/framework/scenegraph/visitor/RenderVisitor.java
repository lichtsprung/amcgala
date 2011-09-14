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

package cga.framework.scenegraph.visitor;

import cga.framework.camera.Camera;
import cga.framework.scenegraph.Node;
import cga.framework.math.Matrix;
import cga.framework.renderer.Renderer;
import cga.framework.shape.Shape;

public class RenderVisitor implements Visitor {

    private Renderer renderer;
    private Camera camera;

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void visit(Node node) {
        Matrix transform = node.getTransformMatrix();        
        for (Shape renderable : node.getGeometry()) {
            renderable.render(transform, getCamera(), getRenderer());
        }
    }
}