package org.amcgala.framework.shape.util.bounds;

import org.amcgala.framework.math.Vector3d;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests für die Klasse {@link BoundingBox}.
 *
 * @author Robert Giacinto
 */
public class BoundingBoxTest {

    private static final Logger log = LoggerFactory.getLogger(BoundingBoxTest.class);
    private BoundingBox box;

    @Test
    public void simpleBoundingBoxTest() {
        log.info("Es wird eine einfache BoundingBox für die Vektoren (-1,-1,-1) und (1,1,1) erzeugt.");
        List<Vector3d> vectors = new ArrayList<Vector3d>(5);
        vectors.add(new Vector3d(-1, -1, -1));
        vectors.add(new Vector3d(1, 1, 1));
        box = new BoundingBox(vectors);
        Assert.assertEquals(2, box.getWidth(), 0.0001);
        log.info("Mittelpunkt liegt bei {}", box.getCenter());
    }
}
