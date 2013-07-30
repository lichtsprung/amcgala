package org.amcgala.testing.agent;

import org.amcgala.AgentClient;
import org.amcgala.agent.World$;

/**
 * Client Main for Java
 */
public class ExampleAgentMain {
    public static void main(String[] args) {
        new AgentClient(3, ExampleAgent.class, World$.MODULE$.RandomIndex());
    }
}
