package org.amcgala.agent

import akka.actor.{ Address, ActorRef, Props, Actor }
import com.typesafe.config.Config
import org.amcgala.agent.SimulationManager.{ SimulationRequest, SimulationResponse }
import org.amcgala.agent.CompetitionManager.{ Application, StartCompetition }
import scala.Application

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

object CompetitionManager {
  case object Application
  case object Start
  case object StartCompetition

  def props(config: Config) = Props(new CompetitionManager(config))
}

class CompetitionManager(config: Config) extends Actor {
  import CompetitionManager._
  import concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  var applicants = Set.empty[ActorRef]
  var simulations = Map.empty[Address, ActorRef]

  def competitionMode: Actor.Receive = {
    case StartCompetition ⇒
      var contestants = applicants.toList

      for (i ← 0 until contestants.size / 2) {
        val sim = context.actorOf(Simulation.props())
        sim ! Simulation.SimulationConfig(config)
        val a = contestants.head
        val b = contestants.tail.head
        contestants = contestants.drop(2)
        simulations = simulations + (a.path.address -> sim)
        simulations = simulations + (b.path.address -> sim)
      }

      applicants.foreach(a ⇒ a ! StartCompetition)

    case SimulationRequest ⇒
      for {
        sim ← simulations.get(sender.path.address)
      } {
        sim forward SimulationRequest
      }
  }

  def receive: Actor.Receive = {
    case Application ⇒
      if (applicants.isEmpty) {
        context.system.scheduler.scheduleOnce(5 minutes, self, Start)
      }
      applicants = applicants + sender
    case Start ⇒
      val simCount = applicants.size / 2
      if (simCount < 1) {
        println(s"Konnte keine Gegner finden...")
        println(s"Resetting...")
        println(s"Waiting...")
        applicants = Set.empty[ActorRef]
      } else {
        if (applicants.size % 2 != 0) {
          println("Ungerade Anzahl von Teilnehmern. Einer vom Wettbewerb ausgenommen.")
          applicants = applicants - applicants.last
        }
        context become competitionMode
        self ! StartCompetition
      }

  }
}

object CompetitionClient {
  def props(agentCls: String, count: Int) = Props(new CompetitionClient(agentCls, count))
}

class CompetitionClient(agentCls: String, count: Int) extends Actor {

  val manager = context.actorSelection(context.system.settings.config.getString("org.amcgala.agent.simulation.remote-address"))

  manager ! Application

  def receive: Actor.Receive = {
    case StartCompetition ⇒
    // spawn n agents of type agentcls
  }
}
