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
import org.amcgala.agent.Agent._
import org.amcgala.agent.utils.PartialFunctionBuilder
import org.amcgala.agent.PaidService.PaidService
import org.amcgala.agent.AgentMessages.PutInformationObjectTo
import org.amcgala.agent.Agent.AgentStates
import org.amcgala.agent.World.Index
import scala.Some
import org.amcgala.agent.World.JCell
import org.amcgala.agent.AgentMessages.ReleasePheromone
import org.amcgala.agent.Simulation.SimulationState
import org.amcgala.agent.World.Cell
import org.amcgala.agent.Simulation.AgentDeath
import org.amcgala.agent.Agent.AgentID
import org.amcgala.agent.Simulation.SimulationConfig
import org.amcgala.agent.Simulation.SimulationUpdate
import org.amcgala.agent.World.CellWithIndex
import org.amcgala.agent.AgentMessages.MoveTo
import org.amcgala.agent.Simulation.RegisterWithDefaultIndex
import org.amcgala.agent.Simulation.RegisterWithRandomIndex
import org.amcgala.agent.AgentMessages.PutInformationObject
import org.amcgala.agent.Simulation.CellChange
import org.amcgala.agent.Simulation.Register
import org.amcgala.agent.World.JCellWithIndex
import org.amcgala.agent.AgentMessages.ChangeValue
import org.amcgala.agent.Agent.Payloads
import org.amcgala.agent.Simulation.AgentStateChange
import org.amcgala.agent.AgentMessages.TakePayload
import org.amcgala.agent.World.WorldInfo
import scala.util.Random

/**
  * Nachricht, die zwischen Agenten und der Simulation verschickt werden können.
  */
trait Message

/**
  * Companion Object der Agentensimulation. Hier finden sich alle Messageklassen, die die Simulation verarbeitet.
  */
object Simulation {

  object Implicits {
    /**
      * Macht aus  einer [[org.amcgala.agent.World.Cell]] eine [[org.amcgala.agent.World.JCell]]
      * @param cell die Zelle, die umgewandelt werden soll
      * @return die [[org.amcgala.agent.World.JCell]]
      */
    implicit def cell2JCell(cell: Cell): JCell = {
      JCell(cell.value, cell.pheromones, cell.informationObjects, cell.payloadObjects, cell.agents)
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
  case class SimulationUpdate(currentPosition: Index, currentCell: JCell, currentState: AgentStates, neighbours: java.util.HashMap[Index, JCellWithIndex]) extends Message

  /**
    * Der globale Zustand der Simulation. Diese Nachricht wird an alle Statelogger geschickt, die die Informationen
    * zur Visualisierung verwenden können.
    *
    * @param worldInfo globale Informationen zur Welt
    * @param agents alle sich in der Simulation befindlichen Agenten
    */
  case class SimulationState(worldInfo: WorldInfo, agents: java.util.List[AgentStates]) extends Message

  trait ChangeMessage extends Message

  /**
    * Ein Agent hat seinen Zustand veraendert. Diese Nachricht wird nach der Aenderung an alle [[org.amcgala.agent.StateLoggerAgent]]s geschickt.
    *
    * @param state der neue Zustand des Agenten
    */
  case class AgentStateChange(state: AgentStates) extends ChangeMessage

  /**
    * Nachricht die an StateLogger verschickt wird, wenn ein Agent aus der Simulation entfernt wurde.
    * @param state der letzte Zustand des entfernten Agenten
    */
  case class AgentDeath(state: AgentStates) extends ChangeMessage

  /**
    * Nachricht, die an StateLogger verschickt wird, wenn ein neuer Agent die Simulation betritt.
    * @param state der Initialzustand des Agenten
    */
  case class AgentBirth(state: AgentStates) extends ChangeMessage

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
  case class SimulationStateUpdate(changedCells: java.util.List[(Index, Cell)], agents: java.util.List[AgentStates]) extends Message

  /**
    * Registriert einen Agenten bei der Simulation. Er wird von der Simulation auf die Zelle mit dem mitgeschickten Index
    * geschickt.
    * Index kann von der Simulation ignoriert werden, wenn individuelle Platzierung in der Konfiguration deaktiviert wurde.
    *
    * @param index der Index der Zelle, auf der der Agent platziert werden soll
    */
  case class Register(index: Index, parentIndex: Index) extends Message

  /**
    * Registriert einen Agenten bei der Simulation. Platziert diesen auf einer zufällig ausgewählten Zelle.
    */
  case class RegisterWithRandomIndex(parentIndex: Index) extends Message

  /**
    * Registriert einen [[org.amcgala.agent.StateLoggerAgent]]en bei der Simulation.
    */
  case object RegisterStateLogger extends Message

  /**
    * Registriert einen [[org.amcgala.agent.AmcgalaAgent]] bei der Simulation und platziert diesen auf der Standardzelle,
    * die in der Konfiguration definiert werden kann.
    */
  case class RegisterWithDefaultIndex(parentIndex: Index) extends Message

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
  private[agent] def props(): Props = Props(new CompetitionSimulation())

}

trait ComposableSimulation extends Actor {
  private val defaultConfig = ConfigFactory.load("amcgala")

  protected var agents = Map.empty[ActorPath, AgentStates]

  protected var world: Option[World] = None

  protected var currentConfig = ConfigFactory.empty().withFallback(defaultConfig)

  def pingTime = currentConfig.getInt("org.amcgala.agent.simulation.update-ping-time").milliseconds

  def pushMode = currentConfig.getBoolean("org.amcgala.agent.simulation.push-mode")

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
  def constraintsChecker: WorldConstraintsChecker = {
    val checkerClass = currentConfig.getString("org.amcgala.agent.simulation.world.constraints.class")
    val cl = ClassLoader.getSystemClassLoader.loadClass(checkerClass)
    cl.newInstance().asInstanceOf[WorldConstraintsChecker]
  }

  /**
    * [[org.amcgala.agent.utils.PartialFunctionBuilder]] der alle [[scala.PartialFunction]]s zusammenfasst, die von der
    * Simulation verarbeitet werden sollen.
    */
  protected lazy val receiveBuilder = new PartialFunctionBuilder[Any, Unit]

  context.become(waitForWorld)

  def waitForWorld: Actor.Receive = {
    case SimulationConfig(config) ⇒
      currentConfig = config.withFallback(defaultConfig)
      world = Some(World(currentConfig))
      if (pushMode) {
        context.system.scheduler.schedule(5 seconds, pingTime, self, Update)
      }
      context.become(receive)
  }

  def agentAt(index: Index): Map[ActorPath, AgentStates] = agents.filter(p ⇒ p._2.position == index)

  final def receive = receiveBuilder.result()
}

/**
  * Ermoeglicht das Verwalten von [[org.amcgala.agent.StateLoggerAgent]]s.
  */
trait StateLoggerHandling {
  comp: ComposableSimulation ⇒
  var stateLogger = Set.empty[ActorRef]

  def stateLoggerUpdatePingTime = currentConfig.getInt("org.amcgala.agent.simulation.statelogger-ping-time").seconds

  receiveBuilder += {

    case RegisterStateLogger if !agents.exists(e ⇒ e._1 == sender) ⇒
      for {
        w ← world
      } {
        sender ! SimulationState(w.worldInfo, agents.values.toList)
        stateLogger = stateLogger + sender
      }

    case change: ChangeMessage ⇒
      stateLogger foreach {
        _ ! change
      }
  }
}

trait InformationObjectHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case PutInformationObjectTo(index, informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(index, informationObject)
        }
      }

    case PutInformationObject(informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(agent.position, informationObject)
        }
      }
  }

}

trait CellChangeHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case ChangeValue(value) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
        cell ← w.get(agent.position)
        nCell ← w.get(agent.position)
      } {
        if (constraintsChecker.checkValueChange(cell.value, value)) {
          w.change(agent.position, value)
          self ! CellChange(agent.position, nCell)
        }
      }
  }
}

trait AgentDeathHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case Death ⇒
      for {
        agent ← agents.get(sender.path)
      } {
        self ! AgentDeath(agent)
        agents = agents - sender.path
        sender ! PoisonPill
      }
  }
}

trait AgentMoveHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case MoveTo(index) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
        oldCell ← w.get(agent.position)
        nCell ← w.get(index)
      } {
        if (constraintsChecker.checkMove(sender, CellWithIndex(agent.position, oldCell), CellWithIndex(index, nCell), agents.values.toList)) {
          val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = index, owner = sender.path.address)
          agents = agents + (sender.path -> agentStates)
          self ! AgentStateChange(agent)
        }
      }
  }
}

trait PheromoneHandling {
  comp: ComposableSimulation ⇒

  protected def pheromoneMode = currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")

  receiveBuilder += {
    case ReleasePheromone(pheromone) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
      } {
        if (pheromoneMode) {
          if (constraintsChecker.checkPheromone(agent, pheromone)) {
            w.addPheromone(agent.position, pheromone)
          }
        }
      }
  }
}

trait UpdateHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case Update ⇒
      world map (w ⇒ {
        if (currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")) {
          w.update()
        }

        agents map {
          case (ref, currentState) ⇒
            import Simulation.Implicits._
            val neighbourCells = w.neighbours(currentState.position)
            val javaNeighbours = new util.HashMap[Index, JCellWithIndex]()

            neighbourCells foreach {
              entry ⇒
                val nAgents = agentAt(entry._2.index)
                javaNeighbours.put(entry._1, JCellWithIndex(entry._2.index, JCell(entry._2.cell.value, entry._2.cell.pheromones, entry._2.cell.informationObjects, entry._2.cell.payloadObjects, nAgents.values.toList)))
            }

            context.actorSelection(ref) ! SimulationUpdate(currentState.position, w.get(currentState.position).get, currentState, javaNeighbours)
        }

      })

    case RequestUpdate ⇒
      import Simulation.Implicits._
      if (!pushMode) {
        for {
          w ← world
          currentState ← agents.get(sender.path)
        } {
          val neighbourCells = w.neighbours(currentState.position)
          val javaNeighbours = new util.HashMap[Index, JCellWithIndex]()

          neighbourCells foreach {
            entry ⇒
              val nAgents = agentAt(entry._2.index)
              javaNeighbours.put(entry._1, JCellWithIndex(entry._2.index, JCell(entry._2.cell.value, entry._2.cell.pheromones, entry._2.cell.informationObjects, entry._2.cell.payloadObjects, nAgents.values.toList)))
          }

          sender ! SimulationUpdate(currentState.position, w.get(currentState.position).get, currentState, javaNeighbours)
        }
      }
  }
}

trait AgentRegisterHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case RegisterWithRandomIndex(parentIndex) ⇒
      world map (w ⇒ {
        val randomCell = w.randomCell
        val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = randomCell.index, owner = sender.path.address)
        agents = agents + (sender.path -> agentStates)
        self ! AgentStateChange(agents(sender.path))
        self.tell(RequestUpdate, sender)
      })

    case Register(index, parentIndex) ⇒
      for {
        w ← world
      } {
        if (!agents.exists(entry ⇒ entry._2.position == index)) {
          val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = index, owner = sender.path.address)
          agents = agents + (sender.path -> agentStates)
          self ! AgentStateChange(agents(sender.path))
          self.tell(RequestUpdate, sender)
        } else {
          sender ! SpawnRejected
        }
      }

    case RegisterWithDefaultIndex(parentIndex) ⇒
      defaultPosition match {
        case World.RandomIndex ⇒
          self forward RegisterWithRandomIndex
        case index: Index ⇒
          world map (w ⇒ {
            val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = index, owner = sender.path.address)
            agents = agents + (sender.path -> agentStates)
            self ! AgentStateChange(agents(sender.path))
            self.tell(RequestUpdate, sender)
          })
      }
  }
}

object PaidService {

  case object PriceListRequest extends Message

  trait PaidService[A, B] {
    comp: ComposableSimulation ⇒

    protected var prices = Map.empty[A, B]
    protected var amounts = Map.empty[Address, B]

    protected def setPrice(entry: (A, B)): Unit = {
      prices = prices + entry
    }

    receiveBuilder += {
      case PriceListRequest ⇒ sender ! prices.toList
    }

  }

}

trait PaidAgentMoveHandling {
  comp: ComposableSimulation with PaidService[Any, Float] with PayloadHandling with StopTimer ⇒

  private val price = 10
  private val honeyPrice = -100
  setPrice(MoveTo -> price)

  receiveBuilder += {
    case MoveTo(index) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
        ncell ← w.get(index)
        oldCell ← w.get(agent.position)
      } {
        if (constraintsChecker.checkMove(sender, CellWithIndex(agent.position, oldCell), CellWithIndex(index, ncell), agents.values.toList)) {
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + price))
          var payloads = agent.payloads
          if (ncell.informationObjects.contains(Base) && payloads.values.contains(Honey)) {
            println("Habe Honig nach Hause gebracht!")
            amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + honeyPrice))
            payloads = Payloads(payloads.values.filterNot(p ⇒ p == Honey).toSet)
          }
          val agentStates = AgentStates(agent.id, index, agent.power, agent.owner, agent.life, payloads)
          agents = agents + (sender.path -> agentStates)
          self ! AgentStateChange(agent)
        }
      }
  }
}

trait PaidCellChangeHandling {
  comp: ComposableSimulation with PaidService[Any, Float] ⇒

  private val price = 2
  setPrice(ChangeValue -> price)

  receiveBuilder += {
    case ChangeValue(value) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
        cell ← w.get(agent.position)
        nCell ← w.get(agent.position)
      } {
        if (constraintsChecker.checkValueChange(cell.value, value)) {
          w.change(agent.position, value)
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + price))
          self ! CellChange(agent.position, nCell)
        }
      }
  }

}

trait PaidInformationObjectHandling {
  comp: ComposableSimulation with PaidService[Any, Float] ⇒

  val pricePutInformationObject = 1f
  val pricePutInformationObjectTo = 1f

  setPrice(PutInformationObject -> pricePutInformationObject)
  setPrice(PutInformationObjectTo -> pricePutInformationObjectTo)

  receiveBuilder += {
    case PutInformationObjectTo(index, informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(index, informationObject)
          self ! CellChange(index, w.get(index).get)
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + pricePutInformationObjectTo))
        }
      }

    case PutInformationObject(informationObject) ⇒
      for {
        w ← world
        agent ← agents.get(sender.path)
      } {
        if (constraintsChecker.checkInformationObject(agent, informationObject)) {
          w.addInformationObject(agent.position, informationObject)
          self ! CellChange(agent.position, w.get(agent.position).get)
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + pricePutInformationObject))
        }
      }
  }

}

trait PaidAgentRegisterHandling {
  comp: ComposableSimulation with PaidService[Any, Float] ⇒

  val spawnPrice = 20
  var homeBases = Map.empty[Address, Index]

  setPrice(RegisterWithRandomIndex -> spawnPrice)
  setPrice(Register -> spawnPrice)
  setPrice(RegisterWithDefaultIndex -> spawnPrice)

  def honeyPosition: Option[Index] = {
    for {
      w ← world
      cellWithIndex ← w.toList.find(cell ⇒ cell.cell.payloadObjects.contains(Honey))
    } {
      return Some(cellWithIndex.index)
    }
    None
  }

  def homeBase(honeyPosition: Index, homeBases: Map[Address, Index], hostAddress: Address): (Option[Index], Map[Address, Index]) = {
    for {
      w ← world
    } {
      val distance = 100
      val xDist = distance - Random.nextInt(distance)
      val yDist = distance - xDist
      val baseIndex = Index((honeyPosition.x + xDist) % w.width, (honeyPosition.y + yDist) % w.height)
      if (homeBases.contains(baseIndex)) {
        return homeBase(honeyPosition, homeBases, hostAddress)
      } else {
        w.addInformationObject(baseIndex, Base)
        self ! CellChange(baseIndex, w.get(baseIndex).get)
        return (Some(baseIndex), homeBases + (hostAddress -> baseIndex))
      }
    }
    (None, homeBases)
  }

  receiveBuilder += {
    case RegisterWithRandomIndex(parentIndex) ⇒
      world map (w ⇒ {
        val randomCell = w.randomCell
        val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = randomCell.index, owner = sender.path.address)
        agents = agents + (sender.path -> agentStates)
        val distance = math.abs(parentIndex.x - randomCell.index.x) + math.abs(parentIndex.y - randomCell.index.y)
        amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + distance * spawnPrice))
        self ! AgentStateChange(agents(sender.path))
        self.tell(RequestUpdate, sender)
      })

    case Register(index, parentIndex) ⇒
      for {
        w ← world
      } {
        if (!agents.exists(entry ⇒ entry._2.position == index)) {
          val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = index, owner = sender.path.address)
          agents = agents + (sender.path -> agentStates)
          val distance = math.abs(parentIndex.x - index.x) + math.abs(parentIndex.y - index.y)
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + distance * spawnPrice))

          self ! AgentStateChange(agents(sender.path))
          self.tell(RequestUpdate, sender)
        } else {
          sender ! SpawnRejected
        }
      }

    case reg @ RegisterWithDefaultIndex(parentIndex) ⇒
      if (homeBases.contains(sender.path.address)) {
        val index = homeBases(sender.path.address)
        world map (w ⇒ {
          val agentStates = AgentStates(id = AgentID(sender.hashCode()), position = index, owner = sender.path.address)
          agents = agents + (sender.path -> agentStates)
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + spawnPrice))
          self ! AgentStateChange(agents(sender.path))
          self.tell(RequestUpdate, sender)
        })
      } else {
        for {
          hp ← honeyPosition
        } {
          val newBase = homeBase(hp, homeBases, sender.path.address)
          homeBases = newBase._2
          self.tell(reg, sender)
        }
      }
  }
}

trait PaidAgentAttackHandling {
  comp: ComposableSimulation with PaidService[Any, Float] with AgentDeathHandling ⇒

  val attackPrice = 20

  setPrice(Attack -> attackPrice)

  receiveBuilder += {
    case Attack(index) ⇒
      for {
        agent ← agents.get(sender.path)
        w ← world
      } {
        if (w.neighbours(agent.position).values.exists(c ⇒ c.index == index)) {
          amounts = amounts + (sender.path.address -> (amounts.getOrElse(sender.path.address, 0f) + attackPrice))
          for {
            a ← agentAt(index)
          } {
            context.actorSelection(a._1) ! PoisonPill
          }
        } else {
          println(s"Illegal target index!")
        }
      }

  }

}

trait SimulationManagerHandling {
  comp: ComposableSimulation ⇒

  import SimulationManager._

  receiveBuilder += {
    case SimulationRequest ⇒
      sender ! SimulationResponse
  }
}

object StopTimer {

  case object StopSimulation

}

trait StopTimer {
  comp: ComposableSimulation with PaidService.PaidService[Any, Float] ⇒

  import StopTimer._

  val stopTime = currentConfig.getInt("org.amcgala.agent.simulation.stop-timer").minute

  context.system.scheduler.scheduleOnce(stopTime, self, StopSimulation)

  receiveBuilder += {
    case StopSimulation ⇒
      println(s"Score: ${amounts.head._2}")
      context.stop(self)

  }
}

trait PayloadHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case TakePayload(payload) ⇒
      for {
        w ← world
        state ← agents.get(sender.path)
        cell ← w.get(state.position)
      } {
        if (cell.payloadObjects.contains(payload)) {
          agents = agents + (sender.path -> AgentStates(state.id, state.position, state.power, state.owner, state.life, Payloads(state.payloads.values + payload)))
        }
      }
  }
}

trait IdleHandling {
  comp: ComposableSimulation ⇒

  receiveBuilder += {
    case Idle ⇒
  }
}

// TODO Sollte nicht hier sein
case object Honey extends Payload

// TODO Sollte nicht hier sein
case object Base extends InformationObject

class DefaultSimulation extends ComposableSimulation
  with StateLoggerHandling with AgentMoveHandling
  with AgentDeathHandling with CellChangeHandling
  with InformationObjectHandling with UpdateHandling
  with AgentRegisterHandling with SimulationManagerHandling with IdleHandling

class CompetitionSimulation extends ComposableSimulation
  with StateLoggerHandling with PaidService[Any, Float] with PaidAgentMoveHandling
  with AgentDeathHandling with PaidInformationObjectHandling with UpdateHandling
  with PaidAgentRegisterHandling with SimulationManagerHandling with PayloadHandling with StopTimer
  with IdleHandling with PaidAgentAttackHandling
