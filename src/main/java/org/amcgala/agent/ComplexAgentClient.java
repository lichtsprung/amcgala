package org.amcgala.agent;

import akka.actor.ActorSystem;

/**
 * Dieser Client kann verwendet werden, um komplexere Verhaltenmuster mit Agents zu implementieren und zu laden.
 */
public class ComplexAgentClient {
    private ActorSystem system = ActorSystem.create("Client");
}
