package org.amcgala.agent

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

/**
 * The Simulation Server.
 */
class AgentServer {
  val config = ConfigFactory.load()
  val system = ActorSystem("Simulator", config.getConfig("simulation"))

  val simulation = system.actorOf(Simulation.props(), "simulation")
}
