package org.amcgala.agent

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import org.amcgala.agent.Simulation._
import scala.util.Random
import org.amcgala.agent.World.{WorldInfo, CellWithIndex, Index, Cell}
import org.amcgala.agent.Agent.MoveTo
import org.amcgala.agent.Agent.AgentState
import org.amcgala.agent.Agent.ChangeValue
import org.amcgala.agent.Simulation.SimulationUpdate
import scala.collection.JavaConversions._


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
  val world = World(800, 600)
  var changedCells = Map.empty[Index, Cell]

  override def preStart() {
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global
    context.system.scheduler.schedule(5.seconds, 100.milliseconds, self, Update)
  }

  def receive: Actor.Receive = simulationLifeCycle orElse handleAgentMessages

  def simulationLifeCycle: Actor.Receive = {
    case RegisterWithRandomIndex =>
      val randomCell = world.randomCell
      agents = agents + (sender -> AgentState(sender.hashCode(), randomCell.index, randomCell.cell))
      log.debug("Registering new Actor at {}", randomCell)
      log.debug("Agents on this cell: {}", agents.filter(entry => entry._2 == randomCell).keySet)

    case Register(index) =>
      agents = agents + (sender -> AgentState(sender.hashCode(), index, world(index)))
      log.info("Registering new Actor at {}", index)

    case RegisterStateLogger if !agents.exists(e => e._1 == sender) =>
      sender ! SimulationState(world.worldInfo, agents.values.toList)
      stateLogger = stateLogger + sender
      log.info(s"Registered StateLoggers: $stateLogger")

    case Update =>
      agents map {
        case (ref, currentState) => {
          val neighbourCells = world.neighbours(currentState.position)
          ref ! SimulationUpdate(currentState, neighbourCells)
        }
      }
      stateLogger map (logger => {
        log.debug(s"Sending SimulationStateUpdate to $logger")
        logger ! SimulationStateUpdate(changedCells.toList, agents.values.toList)
      })
      changedCells = Map.empty[Index, Cell]
  }

  def handleAgentMessages: Actor.Receive = {
    case MoveTo(index) =>
      log.debug("Moving agent {} to {}", sender, index)
      agents = agents + (sender -> AgentState(sender.hashCode(), index, world(index)))

    case ChangeValue(value) =>
      agents.get(sender) map (c => {
        world.change(c.position, Cell(value))
        changedCells = changedCells + (c.position -> Cell(value))
        log.debug("New value at {} is {}", c.position, world(c.position))
      })
  }
}


object World {

  case class WorldInfo(width: Int, height: Int, cells: java.util.List[(Index, Cell)])

  case class Cell(value: Double)

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
          field = field + (Index(x, y) -> Cell(0))
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

  def change(index: Index, cell: Cell) = {
    field = field + (index -> cell)
  }

  def apply(index: Index) = {
    field(index)
  }

  def toList: List[CellWithIndex] = (for (entry <- field) yield CellWithIndex(entry._1, entry._2)).toList

  def worldInfo: WorldInfo = WorldInfo(width, height, field.toList)
}

object Agent {

  sealed trait AgentMessage

  case class MoveTo(index: Index) extends AgentMessage

  case class ChangeValue(newValue: Double) extends AgentMessage

  case class AgentState(id: Int, position: Index, cell: Cell)

}