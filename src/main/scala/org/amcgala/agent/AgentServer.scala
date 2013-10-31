package org.amcgala.agent

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import org.amcgala.agent.Simulation.SimulationConfig

/**
  * The Simulation Server.
  */
class AgentServer(configName: String) {
  val config = ConfigFactory.load()
  val system = ActorSystem("Simulator", config.getConfig("simulation"))

  val simulation = system.actorOf(Simulation.props(), "simulation")
  simulation ! SimulationConfig(ConfigFactory.load(configName))
}
