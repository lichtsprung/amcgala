package org.amcgala.testing.agent;

import org.amcgala.agent.AgentServer;
import org.amcgala.agent.SimpleAgentClient;

/**
 * Client Main for Java
 */
public class ExampleAgentMain {
    public static void main(String[] args) throws Exception {
        new AgentServer("floodfill");
        Thread.sleep(2000);
        new SimpleAgentClient("floodfill");
    }
}
