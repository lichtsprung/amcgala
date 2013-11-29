package org.amcgala.agent

import akka.actor._
import org.amcgala.agent.Simulation._
import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.{ ConfigFactory, Config }
import java.util
import org.amcgala.agent.AgentMessages._
import org.amcgala.agent.World._
import org.amcgala.agent.Agent.{ Pheromone, AgentState, AgentID }
import org.amcgala.agent.World.Index
import scala.Some
import org.amcgala.agent.AgentMessages.ReleasePheromone
import org.amcgala.agent.Simulation.SimulationStateUpdate
import org.amcgala.agent.Simulation.SimulationState
import org.amcgala.agent.World.Cell
import org.amcgala.agent.Simulation.AgentDeath
import org.amcgala.agent.Simulation.SimulationConfig
import org.amcgala.agent.Simulation.SimulationUpdate
import org.amcgala.agent.AgentMessages.MoveTo
import org.amcgala.agent.AgentMessages.PutInformationObject
import org.amcgala.agent.Simulation.CellChange
import org.amcgala.agent.Simulation.Register
import org.amcgala.agent.AgentMessages.ChangeValue
import org.amcgala.agent.Simulation.AgentStateChange
import org.amcgala.agent.World.WorldInfo

trait Message

/**
  * Companion Object der Agentensimulation. Hier finden sich alle Messageklassen, die die Simulation verarbeitet.
  */
object Simulation {

  object Utils {
    implicit def cell2JCell(cell: Cell): JCell = {
      JCell(cell.value, new util.HashMap[Pheromone, Float](), cell.informationObjects)
    }

    implicit def cellWI2JCellWI(cell: CellWithIndex): JCellWithIndex = {

      JCellWithIndex(cell.index, cell2JCell(cell.cell))
    }
  }

  /**
    * Die Konfiguration, die von der Simulation geladen werden soll. In der Regel die erste Nachricht, die an die Simulation
    * geschickt wird, um das Laden einer Welt zu initiieren.
    *
    * @param config die Konfiguration, die gelanden werden soll
    */
  case class SimulationConfig(config: Config) extends Message

  /**
    * Aktueller Zustand eines Agenten. Das Update wird an den entsprechenden Agenten geschickt, der auf die Nachricht
    * reagieren kann.
    *
    * @param currentCell der aktuelle Zustand des Agenten
    * @param neighbours die Nachbarzellen
    */
  case class SimulationUpdate(currentPosition: Index, currentCell: JCell, neighbours: java.util.HashMap[Index, JCellWithIndex]) extends Message

  /**
    * Der globale Zustand der Simulation. Diese Nachricht wird an alle Statelogger geschickt, die die Informationen
    * zur Visualisierung verwenden können.
    *
    * @param worldInfo globale Informationen zur Welt
    * @param agents alle sich in der Simulation befindlichen Agenten
    */
  case class SimulationState(worldInfo: WorldInfo, agents: java.util.List[AgentState]) extends Message

  trait ChangeMessage extends Message

  /**
    * Ein Agent hat seinen Zustand veraendert. Diese Nachricht wird nach der Aenderung an alle [[org.amcgala.agent.StateLoggerAgent]]s geschickt.
    *
    * @param state der neue Zustand des Agenten
    */
  case class AgentStateChange(state: AgentState) extends ChangeMessage

  /**
    * Nachricht die an StateLogger verschickt wird, wenn ein Agent aus der Simulation entfernt wurde.
    * @param state der letzte Zustand des entfernten Agenten
    */
  case class AgentDeath(state: AgentState) extends ChangeMessage

  /**
    * Nachricht, die an StateLogger verschickt wird, wenn ein neuer Agent die Simulation betritt.
    * @param state der Initialzustand des Agenten
    */
  case class AgentBirth(state: AgentState) extends ChangeMessage

  /**
    * Eine Zelle hat ihren Zustand geaendert. Diese Nachricht wird nach der Aenderung an alle [[org.amcgala.agent.StateLoggerAgent]]s geschickt.
    *
    * @param index der Index der Zelle
    * @param cell der neue Zustand der Zelle
    */
  case class CellChange(index: Index, cell: Cell) extends ChangeMessage

  /**
    * Vollstaendiges Update fuer einen [[org.amcgala.agent.StateLoggerAgent]]. Diese Nachricht wird nach der Registrierung
    * eines Agenten verschickt und danach alle 20 Sekunden, um Uebertragungsfehler zu korrieren.
    *
    * @param changedCells die Zellen, die sich seit dem letzten Tick geändert haben
    * @param agents die Agenten, die ihren Zustand geändert haben
    */
  case class SimulationStateUpdate(changedCells: java.util.List[(Index, Cell)], agents: java.util.List[AgentState]) extends Message

  /**
    * Registriert einen Agenten bei der Simulation. Er wird von der Simulation auf die Zelle mit dem mitgeschickten Index
    * geschickt.
    * Index kann von der Simulation ignoriert werden, wenn individuelle Platzierung in der Konfiguration deaktiviert wurde.
    *
    * @param index der Index der Zelle, auf der der Agent platziert werden soll
    */
  case class Register(index: Index) extends Message

  /**
    * Registriert einen Agenten bei der Simulation. Platziert diesen auf einer zufällig ausgewählten Zelle.
    */
  case object RegisterWithRandomIndex extends Message

  /**
    * Registriert einen [[org.amcgala.agent.StateLoggerAgent]]en bei der Simulation.
    */
  case object RegisterStateLogger extends Message

  /**
    * Registriert einen [[org.amcgala.agent.AmcgalaAgent]] bei der Simulation und platziert diesen auf der Standardzelle,
    * die in der Konfiguration definiert werden kann.
    */
  case object RegisterWithDefaultIndex extends Message

  /**
    * Triggert ein Update der Simulation. Das Interval kann in der Konfiguration definiert werden.
    */
  case object Update extends Message

  case object RequestUpdate extends Message

  /**
    * Triggert ein vollstaendiges Update aller StateLogger.
    * @deprecated wurde durch inkrementelles Update ersetzt
    */
  case object StateLoggerUpdate extends Message

  /**
    * Ein Broadcast an alle Agenten in der Simulation.
    * @param message die Nachricht, die an alle Agenten weitergeleitet werden soll.
    */
  case class Broadcast(message: Any) extends Message

  /**
    * Gibt die [[akka.actor.Props]] Instanz zurück, die zur Erstellung eines neuen Simulation Actors benötigt wird
    * @return die Props
    */
  private[agent] def props(): Props = Props(classOf[Simulation])

}

/**
  * Die Simulation ist das Herzstück des Amcgala Multiagent System. Die Simulation kümmert sich um die Verwaltung der
  * aktiven Welt und ist reagiert auf die Aktionen, die ein Agent durchführt.
  */
class Simulation extends Actor with ActorLogging {
  val defaultConfig = ConfigFactory.load("amcgala")

  var agents = Map.empty[ActorRef, AgentState]
  var stateLogger = Set.empty[ActorRef]

  var world: Option[World] = None

  var currentConfig = ConfigFactory.empty().withFallback(defaultConfig)

  def pingTime = currentConfig.getInt("org.amcgala.agent.simulation.update-ping-time").milliseconds

  def stateLoggerUpdatePingTime = currentConfig.getInt("org.amcgala.agent.simulation.statelogger-ping-time").seconds

  def pushMode = currentConfig.getBoolean("org.amcgala.agent.simulation.push-mode")

  def pheromoneMode = currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")

  /**
    * Die Standardposition eines Agenten in der Welt. Dieser Wert wird aus der aktuellen Konfiguration genommen.
    * @return die Standardposition eines Agenten
    */
  lazy val defaultPosition = {
    val l = currentConfig.getIntList("org.amcgala.agent.default-position")
    assert(l.size() == 2)
    World.Index(l(0), l(1))
  }

  /**
    * Der aktive [[org.amcgala.agent.WorldConstraintsChecker]]. Dieser wird bei der Verarbeitung der Agentennachrichten
    * verwendet, um diese auf Plausibilität zu prüfen. Nur wenn der ConstraintsChecker die Aktion eines Agenten zulässt,
    * wird diese auch tatsächlich ausgeführt.
    *
    * @return die Instanz des aktiven [[org.amcgala.agent.WorldConstraintsChecker]]
    */
  lazy val constraintsChecker = {
    val checkerClass = currentConfig.getString("org.amcgala.agent.simulation.world.constraints.class")
    val cl = ClassLoader.getSystemClassLoader.loadClass(checkerClass)
    cl.newInstance().asInstanceOf[WorldConstraintsChecker]
  }

  def receive: Actor.Receive = waitForWorld

  def waitForWorld: Actor.Receive = {
    case SimulationConfig(config) ⇒
      currentConfig = config.withFallback(defaultConfig)
      world = Some(World(currentConfig))
      if (pushMode) {
        context.system.scheduler.schedule(5 seconds, pingTime, self, Update)
      }
      //      context.system.scheduler.schedule(5 seconds, stateLoggerUpdatePingTime, self, StateLoggerUpdate)
      context.become(simulationLifeCycle orElse handleAgentMessages)
  }

  def simulationLifeCycle: Actor.Receive = {
    case RegisterWithRandomIndex ⇒
      world map (w ⇒ {
        val randomCell = w.randomCell
        agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), randomCell.index))
        self ! AgentStateChange(agents(sender))
        self.tell(RequestUpdate, sender)
      })

    case Register(index) ⇒
      for {
        w ← world
      } {
        if (!agents.exists(entry ⇒ entry._2.position == index)) {
          agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index))
          self ! AgentStateChange(agents(sender))
          self.tell(RequestUpdate, sender)
        } else {
          sender ! SpawnRejected
        }
      }

    case RegisterWithDefaultIndex ⇒
      defaultPosition match {
        case World.RandomIndex ⇒
          self forward RegisterWithRandomIndex
        case index: Index ⇒
          world map (w ⇒ {
            agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index))
            self ! AgentStateChange(agents(sender))
            self.tell(RequestUpdate, sender)
          })
      }

    case RegisterStateLogger if !agents.exists(e ⇒ e._1 == sender) ⇒
      for {
        w ← world
      } {
        sender ! SimulationState(w.worldInfo, agents.values.toList)
        stateLogger = stateLogger + sender
      }

    case Update ⇒
      world map (w ⇒ {
        if (currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")) {
          w.update()
        }

        agents map {
          case (ref, currentState) ⇒ {

            val neighbourCells = w.neighbours(currentState.position)
            val javaNeighbours = new util.HashMap[Index, JCellWithIndex]()
            import Simulation.Utils.cellWI2JCellWI
            neighbourCells foreach {
              entry ⇒
                javaNeighbours.put(entry._1, entry._2)
            }

            import Simulation.Utils.cell2JCell
            ref ! SimulationUpdate(currentState.position, w(currentState.position), javaNeighbours)
          }
        }

      })

    case RequestUpdate ⇒
      if (!pushMode) {
        for {
          w ← world
          currentState ← agents.get(sender)
        } {
          val neighbourCells = w.neighbours(currentState.position)
          val javaNeighbours = new util.HashMap[Index, JCellWithIndex]()
          import Simulation.Utils.cellWI2JCellWI
          neighbourCells foreach {
            entry ⇒
              javaNeighbours.put(entry._1, entry._2)
          }

          import Simulation.Utils.cell2JCell
          sender ! SimulationUpdate(currentState.position, w(currentState.position), javaNeighbours)
        }
      }

    case StateLoggerUpdate ⇒
      for {
        w ← world
      } {
        stateLogger foreach {
          _ ! SimulationStateUpdate(w.field.toList, agents.values.toList)
        }
      }

    case change: ChangeMessage ⇒
      stateLogger foreach {
        _ ! change
      }

  }

  def handleAgentMessages: Actor.Receive = {

    case MoveTo(index) ⇒
      for {
        w ← world
        agent ← agents.get(sender)
      } {
        val oldCell = w(agent.position)
        if (constraintsChecker.checkMove(sender, CellWithIndex(agent.position, oldCell), CellWithIndex(index, w(index)), agents.values.toList)) {
          agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index))
          self ! AgentStateChange(agent)
        }
      }

    case ReleasePheromone(pheromone) ⇒
      for {
        w ← world
        agent ← agents.get(sender)
      } {
        if (pheromoneMode) {
          if (constraintsChecker.checkPheromone(agent, pheromone)) {
            w.addPheromone(agent.position, pheromone)
          }
        }
      }

    case Death ⇒
      for {
        agent ← agents.get(sender)
      } {
        self ! AgentDeath(agent)
        agents = agents - sender
        sender ! PoisonPill
      }

    case ChangeValue(value) ⇒
      for {
        w ← world
        agent ← agents.get(sender)
      } {
        val cell = w(agent.position)
        if (constraintsChecker.checkValueChange(cell.value, value)) {
          w.change(agent.position, value)
          val nCell = w(agent.position)
          if (value > 1) println(value)
          self ! CellChange(agent.position, nCell)
        }
      }

    case PutInformationObject(informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(agent.position, informationObject)
        }
      }

    case PutInformationObjectTo(index, informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(index, informationObject)
        }
      }
  }
}

