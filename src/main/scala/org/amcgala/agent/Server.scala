package org.amcgala.agent

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

/**
 * The Simulation Server.
 */
object Server extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("Simulator", config.getConfig("simulation"))

  val simulation = system.actorOf(Simulation.props(), "simulation")
}
