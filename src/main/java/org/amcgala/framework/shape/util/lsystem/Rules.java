package org.amcgala.framework.shape.util.lsystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Die Ersetzungsregeln eines {@link LindenmayerSystem}.
 *
 * @author Robert Giacinto
 * @since 2.0
 */
public class Rules {
    private Map<String, String> replacementRules;
    private Map<String, String> drawingRules;

    public Rules() {
        replacementRules = new HashMap<String, String>(15);
        drawingRules = new HashMap<String, String>(15);
    }

    public Rules addReplacementRule(String symbol, String replacement) {
        replacementRules.put(symbol, replacement);
        return this;
    }

    public Rules addDrawingRule(String symbol, String replacement) {
        drawingRules.put(symbol, replacement);
        return this;
    }

    public String applyReplacementRules(String symbol) {
        return replacementRules.containsKey(symbol) ? replacementRules.get(symbol) : symbol;
    }

    public String applyDrawingRules(String symbol) {
        return drawingRules.containsKey(symbol) ? drawingRules.get(symbol) : symbol;
    }
}
