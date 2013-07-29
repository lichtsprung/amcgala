package org.amcgala.agents

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Client for the Amcgala Agent Simulation.
 */
class Client(agent: Class[_], initialNumberOfAgents: Int, args: Option[Seq[Any]]) {
  val config = ConfigFactory.load()
  val system = ActorSystem("Client", config.getConfig("client"))

  println(s"creating $initialNumberOfAgents agents of type $agent")
  args match {
    case Some(args) =>
      (0 until initialNumberOfAgents) foreach (i => system.actorOf(Props(agent, args)))
    case None =>
      (0 until initialNumberOfAgents) foreach (i => system.actorOf(Props(agent)))
  }
}




