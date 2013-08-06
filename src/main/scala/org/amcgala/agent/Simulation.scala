package org.amcgala.agent

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import org.amcgala.agent.Simulation._
import scala.util.Random
import org.amcgala.agent.World.Cell
import org.amcgala.agent.Agent._
import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.amcgala.agent.World.CellWithIndex
import org.amcgala.agent.Agent.MoveTo
import org.amcgala.agent.Agent.AgentState
import org.amcgala.agent.World.Index
import org.amcgala.agent.Simulation.Register
import org.amcgala.agent.Simulation.SimulationStateUpdate
import org.amcgala.agent.Simulation.SimulationState
import org.amcgala.agent.Agent.ChangeValue
import org.amcgala.agent.World.WorldInfo
import org.amcgala.agent.Agent.AgentID
import org.amcgala.agent.Simulation.SimulationUpdate


object Simulation {

  sealed trait SimulationMessage

  case class SimulationUpdate(currentState: AgentState, neighbours: Array[CellWithIndex]) extends SimulationMessage

  case class SimulationState(worldInfo: WorldInfo, agents: java.util.List[AgentState]) extends SimulationMessage

  case class SimulationStateUpdate(changedCells: java.util.List[(Index, Cell)], agents: java.util.List[AgentState]) extends SimulationMessage

  case class Register(index: Index) extends SimulationMessage

  case object RegisterWithRandomIndex extends SimulationMessage

  case object RegisterStateLogger extends SimulationMessage

  case object Update extends SimulationMessage

  def props(): Props = Props(classOf[Simulation])

}


/**
 * A [[org.amcgala.agent.Simulation]]
 * <ul>ey
 * <li>manages all Cells in the Simulation</li>
 * <li>manages all living Agents in the Simulation</li>
 * </ul>
 */
class Simulation extends Actor with ActorLogging {
  var agents = Map.empty[ActorRef, AgentState]
  var stateLogger = Set.empty[ActorRef]
  val world = World(200, 150)
  val pingTime = 200 milliseconds

  override def preStart(): Unit = {

    context.system.scheduler.schedule(5 seconds, pingTime, self, Update)
  }

  def receive: Actor.Receive = simulationLifeCycle orElse handleAgentMessages

  def simulationLifeCycle: Actor.Receive = {
    case RegisterWithRandomIndex =>
      val randomCell = world.randomCell
      agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), randomCell.index, randomCell.cell))
      log.debug("Registering new Actor at {}", randomCell)
      log.debug("Agents on this cell: {}", agents.filter(entry => entry._2 == randomCell).keySet)

    case Register(index) =>
      agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index, world(index)))
      log.info("Registering new Actor at {}", index)

    case RegisterStateLogger if !agents.exists(e => e._1 == sender) =>
      sender ! SimulationState(world.worldInfo, agents.values.toList)
      stateLogger = stateLogger + sender
      log.info(s"Registered StateLoggers: $stateLogger")

    case Update =>
      world.update()
      agents map {
        case (ref, currentState) => {
          val neighbourCells = world.neighbours(currentState.position)
          ref ! SimulationUpdate(currentState, neighbourCells)
        }
      }
      stateLogger map (logger => {
        log.debug(s"Sending SimulationStateUpdate to $logger")
        logger ! SimulationStateUpdate(world.field.toList, agents.values.toList)
      })

  }

  def handleAgentMessages: Actor.Receive = {
    case MoveTo(index) =>
      log.debug("Moving agent {} to {}", sender, index)
      agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index, world(index)))

    case ReleasePheromone(pheromone) =>
      agents.get(sender) map (i => world.addPheromone(i.position, pheromone))

    case ChangeValue(value) =>
      agents.get(sender) map (c => {
        world.change(c.position, value)
      })
  }
}


object World {

  type PheromoneMap = Map[Pheromone, Float]

  case class WorldInfo(width: Int, height: Int, cells: java.util.List[(Index, Cell)])

  case class Cell(value: Float, pheromones: PheromoneMap)

  case class CellWithIndex(index: Index, cell: Cell)

  case class Index(x: Int, y: Int)

  val RandomIndex = Index(-1, -1)


  def apply(width: Int, height: Int) = {
    val _width = width
    val _height = height
    new World {
      val width: Int = _width
      val height: Int = _height

      var field: Map[Index, Cell] = Map.empty[Index, Cell]


      for (x <- 0 until width) {
        for (y <- 0 until height) {
          field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
        }
      }

      val neighbours: List[Index] = List(
        Index(1, 0),
        Index(1, 1),
        Index(0, 1),
        Index(-1, 1),
        Index(-1, 0),
        Index(-1, -1),
        Index(0, -1),
        Index(1, -1)
      )
    }
  }
}

trait World {
  val width: Int
  val height: Int
  var field: Map[Index, Cell]
  val neighbours: List[Index]

  def randomCell: CellWithIndex = {
    val col = Random.nextInt(width)
    val row = Random.nextInt(height)
    val i = Index(col, row)

    CellWithIndex(i, field(i))
  }

  def neighbours(index: Index): Array[CellWithIndex] = {
    val neighbourCells = Array.ofDim[CellWithIndex](neighbours.length)

    neighbours.view.zipWithIndex.foreach {
      case (value, i) =>
        val ix = (((index.x + value.x) % width) + width) % width
        val iy = (((index.y + value.y) % height) + height) % height
        val nx = Index(ix, iy)

        neighbourCells(i) = CellWithIndex(nx, field(nx))
    }
    neighbourCells
  }

  def change(index: Index, newValue: Float) = {
    val c = field(index)
    field = field + (index -> Cell(newValue, c.pheromones))
  }

  def addPheromone(index: Index, pheromone: Pheromone) = {
    val c = field(index)
    val nv = math.min(1f, pheromone.strength + c.pheromones.getOrElse(pheromone, 0.0f))
    val pheromones = c.pheromones + (pheromone -> nv)
    field = field + (index -> Cell(c.value, pheromones))
  }

  def apply(index: Index) = {
    field(index)
  }

  def toList: List[CellWithIndex] = (for (entry <- field) yield CellWithIndex(entry._1, entry._2)).toList

  def worldInfo: WorldInfo = WorldInfo(width, height, field.toList)

  def update(): Unit = {
    var newField = Map.empty[Index, Cell]

    field map {
      e =>
        val n = neighbours(e._1) // neighbours of current cell
      var currentCellPheromones = newField.getOrElse(e._1, Cell(0, Map.empty[Pheromone, Float])).pheromones // already updated pheromone values
      val currentCellValue = field(e._1).value // value of current cell

        e._2.pheromones map {
          p =>
            val decay = p._2 * p._1.decayRate // new value of this pheromone after decay
          val sum = decay + currentCellPheromones.getOrElse(p._1, 0.0f) // sum of values (this cell + this pheromone spread from neighbour cells)
            if (sum > 0.009) {
              currentCellPheromones = currentCellPheromones + (p._1 -> math.min(1f, sum))
            }
            val spread = p._2 * p._1.spreadRate
            n map {
              neighbour =>
                val neighbourCell = newField.getOrElse(e._1, Cell(field(neighbour.index).value, Map.empty[Pheromone, Float]))
                var neighbourPheromones = neighbourCell.pheromones
                val sum = spread + neighbourPheromones.getOrElse(p._1, 0.0f)
                if (sum > 0.009) {
                  neighbourPheromones = neighbourPheromones + (p._1 -> math.min(1f, sum))
                }
                newField = newField + (neighbour.index -> Cell(field(neighbour.index).value, neighbourPheromones))
            }
        }
        newField = newField + (e._1 -> Cell(currentCellValue, currentCellPheromones))
    }
    field = newField
  }
}

object Agent {

  trait Pheromone {
    val strength: Float
    val decayRate: Float
    val spreadRate: Float
  }

  case class OwnerPheromone(id: AgentID, strength: Float = 1f, decayRate: Float = 0.66f, spreadRate: Float = 0.09f) extends Pheromone

  case class AgentID(id: Int)

  sealed trait AgentMessage

  case class MoveTo(index: Index) extends AgentMessage

  case class ChangeValue(newValue: Float) extends AgentMessage

  case class ReleasePheromone(pheromone: Pheromone) extends AgentMessage

  case class AgentState(id: AgentID, position: Index, cell: Cell)

}