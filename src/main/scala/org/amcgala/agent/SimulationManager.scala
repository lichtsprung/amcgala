package org.amcgala.agent

import akka.actor.{ ActorRef, Props, Actor }
import com.typesafe.config.Config

object SimulationManager {
  case object SimulationRequest
  case class SimulationCreation(config: Config)
  case object SimulationResponse
  def props() = Props(new SimulationManager())
}

class SimulationManager extends Actor {
  import SimulationManager._

  var simulation: Option[ActorRef] = None

  def receive: Actor.Receive = {
    case SimulationCreation(config) ⇒
      val sim = context.system.actorOf(Simulation.props())
      sim ! Simulation.SimulationConfig(config)
      simulation = Some(sim)
    case SimulationRequest ⇒
      for {
        sim ← simulation
      } {
        sim forward SimulationRequest
      }

  }
}

class CompetitionManager {

}
