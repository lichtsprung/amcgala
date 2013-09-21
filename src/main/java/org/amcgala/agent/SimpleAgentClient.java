package org.amcgala.agent;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

/**
 *
 */
public class SimpleAgentClient {

    private ActorSystem system;

    public SimpleAgentClient(String agentConfiguration) {
        Config config = ConfigFactory.load().getConfig("client").withFallback(ConfigFactory.load(agentConfiguration)).withFallback(ConfigFactory.load("simulation"));
        System.out.println(config.getString("org.amcgala.agent.simulation"));
        system = ActorSystem.create("Client", config);
        List<List<String>> lists = (List<List<String>>) config.getAnyRefList("org.amcgala.agent.client.agents");
        for (List<String> l : lists) {
            try {
                int noa = Integer.parseInt(l.get(0));
                System.out.println("Trying: " + l.get(1));
                Class agentClass = ClassLoader.getSystemClassLoader().loadClass(l.get(1));
                createAgents(noa, agentClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void createAgents(int numberOfAgents, Class<? extends UntypedActor> agentClass) {
        for (int i = 0; i < numberOfAgents; i++) {
            system.actorOf(Props.create(agentClass));
        }
    }
}
