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

import org.amcgala.framework.scenegraph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Der UpdateVisitor traversiert den Szenengraph und ruft die update Methode aller Objekte auf, die das Interface
 * Updatable implementieren.
 *
 * @author Robert Giacinto
 */
public class UpdateVisitor implements Visitor {
    private static final Logger log = LoggerFactory.getLogger(UpdateVisitor.class);
    private boolean paused;


    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void visit(Node node) {
        if (!paused) {
            node.update();
        }
    }
}
