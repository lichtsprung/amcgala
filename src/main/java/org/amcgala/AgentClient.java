package org.amcgala;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 *
 *
 */
public class AgentClient {
    private int numberOfAgents;
    private Class<? extends UntypedActor> agentClass;

    private Config config = ConfigFactory.load();
    private ActorSystem system = ActorSystem.create("Client", config.getConfig("client"));

    public AgentClient(int numberOfAgents, Class<? extends UntypedActor> agentClass) {
        this.numberOfAgents = numberOfAgents;
        this.agentClass = agentClass;
        for (int i = 0; i < numberOfAgents; i++) {
            system.actorOf(Props.create(agentClass));
        }
    }

    public AgentClient(int numberOfAgents, Class<? extends UntypedActor> agentClass, Object... args) {
        this.numberOfAgents = numberOfAgents;
        this.agentClass = agentClass;
        for (int i = 0; i < numberOfAgents; i++) {
            system.actorOf(Props.create(agentClass, args));
        }
    }
}
