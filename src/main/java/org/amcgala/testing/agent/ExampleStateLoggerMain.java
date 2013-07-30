package org.amcgala.testing.agent;

import org.amcgala.AgentClient;

/**
 *
 */
public class ExampleStateLoggerMain {
    public static void main(String[] args) {
        new AgentClient(1, ExampleStateLogger.class);
    }
}
