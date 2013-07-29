package org.amcgala.agents

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Client for the Amcgala Agent Simulation.
 */
class Client(agent: Class[_], initialNumberOfAgents: Int) {
  val config = ConfigFactory.load()
  val system = ActorSystem("Client", config.getConfig("client"))

  println(s"creating $initialNumberOfAgents agents of type $agent")
  (0 until initialNumberOfAgents) foreach (i => system.actorOf(Props(agent)))
}




