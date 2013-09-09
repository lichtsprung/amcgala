package org.amcgala.testing.agent;

import org.amcgala.AgentClient;
import org.amcgala.agent.AgentServer;

/**
 * Client Main for Java
 */
public class ExampleAgentMain {
    public static void main(String[] args) throws Exception {
        new AgentServer("floodfill");
        Thread.sleep(2000);
        new AgentClient("floodfill");
    }
}
