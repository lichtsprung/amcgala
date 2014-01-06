package org.amcgala.agent

import akka.actor._
import com.typesafe.config.{ ConfigFactory, Config }
import org.amcgala.agent.SimulationManager.{ SimulationCreation, SimulationRequest }
import org.amcgala.agent.CompetitionManager.{ Application, StartCompetition }
import scala.Some
import java.lang.Class
import org.amcgala.agent.CompetitionClient.EndRound
import org.amcgala.agent.AgentMessages.NextRound
import scala.util.Sorting

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
      println(s"Creating new simulation...")
      val simCls = config.getString("org.amcgala.agent.simulation.definition.class")
      val clazz = ClassLoader.getSystemClassLoader.loadClass(simCls)
      val sim = context.system.actorOf(Props(clazz))
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

  def props() = Props(new CompetitionManager())
}

class CompetitionManager extends Actor {

  import CompetitionManager._
  import concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  var applicants = Set.empty[ActorRef]
  var simulations = Map.empty[Address, ActorRef]
  var config = ConfigFactory.empty()

  var stashedRequests = List.empty[ActorRef]

  var results = Map.empty[Address, Float]
  var resultCounter = 0
  var simulationCount = 0

  println(s"Manager started. I am ${self.path}")
  println("Waiting for contestants...")

  def competitionMode: Actor.Receive = {
    case StartCompetition ⇒
      var contestants = applicants.toList
      assert(contestants.size % 2 == 0)
      simulationCount = contestants.size / 2

      for (i ← 0 until contestants.size / 2) {

        val simCls = config.getString("org.amcgala.agent.simulation.definition.class")
        val clazz = ClassLoader.getSystemClassLoader.loadClass(simCls)
        val sim = context.actorOf(Props(clazz))
        sim ! Simulation.SimulationConfig(config)

        val a = contestants.head
        val b = contestants.tail.head
        contestants = contestants.drop(2)
        simulations = simulations + (a.path.address -> sim)
        simulations = simulations + (b.path.address -> sim)
      }

      print("Unstashing SimulationRequests...")
      stashedRequests foreach {
        ref ⇒
          self.tell(SimulationRequest, ref)
      }
      println("done")

      print("Starting Competition...")
      applicants foreach (_ ! StartCompetition)
      println("done")

    case SimulationRequest ⇒
      for {
        sim ← simulations.get(sender.path.address)
      } {
        sim forward SimulationRequest
      }

    case StopTimer.SimulationResults(r) ⇒

      println("Simulation stopped.")
      results = results ++ r
      resultCounter += 1
      if (resultCounter == simulationCount) {
        println("Ending round")

        println("Killing old simulations")
        context.children foreach (_ ! PoisonPill)

        stashedRequests = List.empty[ActorRef]
        resultCounter = 0
        simulationCount = 0

        applicants foreach (_ ! EndRound)
        val ranking = results.toArray
        Sorting.quickSort(ranking)(new Ordering[(Address, Float)] {
          def compare(x: (Address, Float), y: (Address, Float)): Int = math.signum(x._2 - y._2).toInt
        })
        ranking foreach (println(_))
        context become round
        self ! Start
      }
  }

  def round: Actor.Receive = {
    case SimulationRequest ⇒
      println(s"Stashing request from $sender")
      stashedRequests = sender :: stashedRequests
    case SimulationCreation(c) ⇒
      config = c
    case Start ⇒
      val simCount = applicants.size / 2
      if (simCount < 1) {
        println(s"And the winner is: ${applicants.head}")
      } else {
        if (applicants.size % 2 != 0) {
          println("Ungerade Anzahl von Teilnehmern. Einer vom Wettbewerb ausgenommen.")
          applicants = applicants - applicants.last
        }
        println("Switching to competition mode...")
        context become competitionMode
        self ! StartCompetition
      }
  }

  def receive: Actor.Receive = {
    case SimulationRequest ⇒
      println(s"Stashing request from $sender")
      stashedRequests = sender :: stashedRequests
    case SimulationCreation(c) ⇒
      config = c
    case Application ⇒
      if (applicants.isEmpty) {
        context.system.scheduler.scheduleOnce(1 minutes, self, Start)
        println("Started application timer... 1 minute remaining.")
      }
      applicants = applicants + sender
      println(s"Added contestant: $sender")
    case Start ⇒
      val simCount = applicants.size / 2
      if (simCount < 1) {
        println(s"Konnte keine Gegner finden.")
        print(s"Resetting...")
        applicants = Set.empty[ActorRef]
        println("done")
      } else {
        if (applicants.size % 2 != 0) {
          println("Ungerade Anzahl von Teilnehmern. Einer vom Wettbewerb ausgenommen.")
          applicants = applicants - applicants.last
        }
        println("Switching to competition mode...")
        context become competitionMode
        self ! StartCompetition
      }

  }
}

object CompetitionClient {
  def props(agents: List[(Class[_], Int)], managerPath: String) = Props(new CompetitionClient(agents, managerPath: String))

  case object EndRound

}

class CompetitionClient(agents: List[(Class[_], Int)], managerPath: String) extends Actor {

  import CompetitionClient._

  val manager = context.actorSelection(managerPath)
  var firstRound = true

  println(s"Sending application to $manager")
  manager ! Application

  def receive: Actor.Receive = {
    case StartCompetition ⇒
      if (firstRound) {
        println("Creating competing agents...")
        for (agent ← agents) {
          val count = agent._2
          val cls = agent._1

          for (i ← 0 until count) {
            context.actorOf(Props(cls))
          }
        }
      } else {
        println("Creating competing agents without Statelogger...")
        for (agent ← agents) {
          val count = agent._2
          val cls = agent._1

          if (!cls.getSuperclass.eq(classOf[StateLoggerAgent])) {
            for (i ← 0 until count) {
              context.actorOf(Props(cls))
            }
          } else {
            println("Skipping Logger!")
          }
        }
      }
    case EndRound ⇒
      println("Ending round...")
      if (firstRound) firstRound = false
      context.children.foreach(_ ! NextRound)
  }
}

class CompetitionApp(agentConfig: String) {
  val AmcgalaAgentCls = classOf[AmcgalaAgent]
  val StateloggerAgentCls = classOf[StateLoggerAgent]
  val UntypedActorCls = classOf[Actor]
  val config = ConfigFactory.load.getConfig("client").withFallback(ConfigFactory.load(agentConfig).withFallback(ConfigFactory.load("amcgala")))
  val localMode = config.getBoolean("org.amcgala.agent.simulation.local-mode")
  val system = ActorSystem("Client", config)
  var managerPath = ""

  if (localMode) {
    val managerCls = ClassLoader.getSystemClassLoader.loadClass(config.getString("org.amcgala.agent.simulation.manager.definition.class"))
    val manager = system.actorOf(Props(managerCls), "simulation-manager")
    manager ! SimulationCreation(ConfigFactory.load(agentConfig))
    managerPath = config.getString("org.amcgala.agent.simulation.local-address")
  } else {
    managerPath = config.getString("org.amcgala.agent.simulation.remote-address")
  }
  val agents = config.getAnyRefList("org.amcgala.agent.client.agents").asInstanceOf[java.util.List[java.util.List[String]]]

  import scala.collection.JavaConversions._

  var a = List.empty[(Class[_], Int)]

  for (l ← agents) {
    try {
      val numberOfAgents: Int = Integer.parseInt(l.get(0))
      val agentClass = ClassLoader.getSystemClassLoader.loadClass(l.get(1))
      a = (agentClass, numberOfAgents) :: a
    } catch {
      case e: ClassNotFoundException ⇒
        println(s"Class could not be found: ${e.getMessage}")
        e.printStackTrace()
    }
  }

  system.actorOf(CompetitionClient.props(a, managerPath))

}
