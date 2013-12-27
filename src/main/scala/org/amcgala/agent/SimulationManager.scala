package org.amcgala.agent

import akka.actor._
import com.typesafe.config.{ ConfigFactory, Config }
import org.amcgala.agent.SimulationManager.{ SimulationCreation, SimulationRequest, SimulationResponse }
import org.amcgala.agent.CompetitionManager.{ Application, StartCompetition }
import scala.Application
import scala.Some
import java.lang.Class

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
      simulationCount = contestants.size /2

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
      applicants.foreach(a ⇒ a ! StartCompetition)
      println("done")

    case SimulationRequest ⇒
      println("Received Simulation request...")
      for {
        sim ← simulations.get(sender.path.address)
      } {
        sim forward SimulationRequest
      }

    case StopTimer.SimulationResults(r) =>
      results = results ++ r
      resultCounter += 1
      if(resultCounter == simulationCount){
        context.children foreach (_ ! PoisonPill)
        // TODO Reset everything and start new round until there are only two agents left
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
  def props(agentCls: Class[_], count: Int, managerPath: String) = Props(new CompetitionClient(agentCls, count, managerPath: String))
}

class CompetitionClient(agentCls: Class[_], count: Int, managerPath: String) extends Actor {

  val manager = context.actorSelection(managerPath)

  println(s"Sending application to $manager")
  manager ! Application

  def receive: Actor.Receive = {
    case StartCompetition ⇒
      println("Creating competing agents...")
      for (i ← 0 until count) {
        context.actorOf(Props(agentCls))
      }
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
  val lists = config.getAnyRefList("org.amcgala.agent.client.agents").asInstanceOf[java.util.List[java.util.List[String]]]

  import scala.collection.JavaConversions._

  for (l ← lists) {
    try {
      val numberOfAgents: Int = Integer.parseInt(l.get(0))
      val agentClass = ClassLoader.getSystemClassLoader.loadClass(l.get(1))
      createAgents(numberOfAgents, agentClass)
    } catch {
      case e: ClassNotFoundException ⇒
        println(s"Class could not be found: ${e.getMessage}")
        e.printStackTrace()
    }
  }

  def createAgents(numberOfAgents: Int, agentClass: Class[_]) = {
    agentClass.getSuperclass match {
      case AmcgalaAgentCls ⇒
        system.actorOf(CompetitionClient.props(agentClass, numberOfAgents, managerPath))
      case _ ⇒
        for (i ← 0 until numberOfAgents) {
          system.actorOf(Props(agentClass))
        }
    }
  }
}
