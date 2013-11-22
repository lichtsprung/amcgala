package org.amcgala.agent;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.ActorEventBus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

/**
 *
 */
public class AgentClient {

    private ActorSystem system;

    public AgentClient(String agentConfiguration) {
        Config config = ConfigFactory.load().getConfig("client").withFallback(ConfigFactory.load(agentConfiguration).withFallback(ConfigFactory.load("amcgala")));
        boolean localMode = config.getBoolean("org.amcgala.agent.simulation.local-mode");

        if (localMode) {
            system = ActorSystem.create("Client", config);
            ActorRef simulation = system.actorOf(Props.create(Simulation.class), "simulation");
            simulation.tell(new Simulation.SimulationConfig(ConfigFactory.load(agentConfiguration)), ActorRef.noSender());
        }else{
            system = ActorSystem.create("Client", config);
        }

        List<List<String>> lists = (List<List<String>>) config.getAnyRefList("org.amcgala.agent.client.agents");
        for (List<String> l : lists) {
            try {
                int noa = Integer.parseInt(l.get(0));
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
