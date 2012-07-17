package org.amcgala.framework;

import org.amcgala.framework.scenegraph.DefaultSceneGraph;
import org.amcgala.framework.scenegraph.SceneGraph;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Tests, die die Funktionalität der Klasse {@link org.amcgala.framework.scenegraph.SceneGraph} sicherstellen sollen.
 *
 * @author Robert Giacinto
 */
public class SceneGraphTest {

    private Logger log = LoggerFactory.getLogger(SceneGraphTest.class);
    private SceneGraph sceneGraph;

    @Before
    public void setup() {
        sceneGraph = new DefaultSceneGraph();
    }


    @Test
    public void emptySceneGraph() {
        log.info("Ein leerer Szenengraph hat nur einen Knoten mit dem Namen 'root'");
        int nodeCount = sceneGraph.getNodeCount();
        assertEquals(nodeCount, 1);
    }
}
