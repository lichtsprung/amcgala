package org.amcgala.testing.agent;

import org.amcgala.AgentClient;
import org.amcgala.agent.AgentServer;
import org.amcgala.agent.World$;

/**
 * Client Main for Java
 */
public class ExampleAgentMain {
    public static void main(String[] args) throws Exception {
        new AgentServer();
        Thread.sleep(2000);
        new AgentClient(1, ExampleStateLogger.class);
        Thread.sleep(2000);
        new AgentClient(10, SimpleAgent.class, World$.MODULE$.RandomIndex());
    }
}
