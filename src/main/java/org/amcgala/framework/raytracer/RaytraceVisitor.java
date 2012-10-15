/*
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.amcgala.framework.raytracer;

import ij.ImageJ;
import org.amcgala.framework.scenegraph.Node;
import org.amcgala.framework.scenegraph.visitor.Visitor;

import javax.swing.JFrame;

import java.awt.Image;
import java.awt.font.ImageGraphicAttribute;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Der RaytraceVisitor traversiert den {@link org.amcgala.framework.scenegraph.SceneGraph} und berechnet die Schnittpunkte
 * den Objekten innerhalb der Szene.
 *
 * @author Robert Giacinto
 * @since 2.1
 */
public class RaytraceVisitor implements Visitor {

    private JFrame frame;
   

    public RaytraceVisitor(JFrame frame) {
        this.frame = checkNotNull(frame);
    }

    @Override
    public void visit(Node node) {
        synchronized (node.getShapes()) {

        }
    }
}
