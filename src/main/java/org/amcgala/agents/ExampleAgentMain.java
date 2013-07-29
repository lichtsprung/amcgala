package org.amcgala.agents;

import org.amcgala.AgentClient;

/**
 * Client Main for Java
 */
public class ExampleAgentMain {
    public static void main(String[] args) {
        new AgentClient(3, ExampleAgent.class, World$.MODULE$.RandomIndex());
    }
}
