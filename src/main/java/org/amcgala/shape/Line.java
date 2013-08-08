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
package org.amcgala.shape;

import org.amcgala.Framework;
import org.amcgala.camera.Camera;
import org.amcgala.math.Vertex3f;
import org.amcgala.renderer.DisplayList;
import org.amcgala.shape.primitives.LinePrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eine Linie im 3d Raum.
 *
 * @author Robert Giacinto
 */
public class Line extends AbstractShape {
    private static final Logger log = LoggerFactory.getLogger(Line.class.getName());
    protected Vertex3f a, b;
    private Camera cam = Framework.getInstance().getActiveScene().getCamera();

    public Line(Vertex3f a, Vertex3f b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public DisplayList getDisplayList(DisplayList list) {
        LinePrimitive linePrimitive = new LinePrimitive(a, b);
        linePrimitive.color = getColor();
        list.lines.add(linePrimitive);
        return list;
    }
}
