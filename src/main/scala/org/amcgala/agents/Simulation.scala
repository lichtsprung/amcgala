package org.amcgala.agents

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import org.amcgala.agents.Simulation._
import scala.util.Random
import org.amcgala.agents.World.{WorldInfo, CellWithIndex, Index, Cell}
import org.amcgala.agents.Agent.MoveTo
import org.amcgala.agents.Agent.AgentState
import org.amcgala.agents.Agent.ChangeValue
import org.amcgala.agents.Simulation.SimulationUpdate
import scala.collection.mutable
import scala.collection.JavaConversions._


object Simulation {

  sealed trait SimulationMessage

  case class SimulationUpdate(currentState: AgentState, neighbours: Array[CellWithIndex]) extends SimulationMessage

  case class SimulationState(worldInfo: WorldInfo, agents: java.util.List[AgentState]) extends SimulationMessage

  case class SimulationStateUpdate(changedCells: java.util.List[CellWithIndex], agents: java.util.List[AgentState]) extends SimulationMessage

  case object Register extends SimulationMessage

  case object RegisterStateLogger extends SimulationMessage

  case object Update extends SimulationMessage

  def props(): Props = Props(classOf[Simulation])

}


/**
 * A [[org.amcgala.agents.Simulation]]
 * <ul>ey
 * <li>manages all Cells in the Simulation</li>
 * <li>manages all living Agents in the Simulation</li>
 * </ul>
 */
class Simulation extends Actor with ActorLogging {
  var agents = Map.empty[ActorRef, AgentState]
  var stateLogger = Set.empty[ActorRef]
  val world = World(800, 600)
  var changedCells = List.empty[CellWithIndex]

  override def preStart() {
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global
    context.system.scheduler.schedule(5.seconds, 200.milliseconds, self, Update)
  }

  def receive: Actor.Receive = simulationLifeCycle orElse handleAgentMessages

  def simulationLifeCycle: Actor.Receive = {
    case Register =>
      val randomCell = world.randomCell
      agents = agents + (sender -> AgentState(randomCell))
      log.info("Registering new Actor at {}", randomCell)
      log.info("Agents on this cell: {}", agents.filter(entry => entry._2 == randomCell).keySet)
    case RegisterStateLogger if !agents.exists(e => e._1 == sender) =>
      sender ! SimulationState(world.worldInfo, agents.values.toList)
      stateLogger = stateLogger + sender
      log.info(s"Registered StateLoggers: $stateLogger")
    case Update =>
      agents map {
        case (ref, currentState) => {
          val neighbourCells = world.neighbours(currentState.position.index)
          ref ! SimulationUpdate(currentState, neighbourCells)
        }
      }
      stateLogger map (logger => {
        log.info(s"Sending SimulationStateUpdate to $logger")

        logger ! SimulationStateUpdate(changedCells, agents.values.toList)
      })
      changedCells = List.empty[CellWithIndex]
  }

  def handleAgentMessages: Actor.Receive = {
    case MoveTo(cell) =>
      log.debug("Moving agent {} to {}", sender, cell)
      agents = agents + (sender -> AgentState(cell))

    case ChangeValue(value) =>
      agents.get(sender) map (c => {
        world.change(c.position.index, Cell(value))
        changedCells = CellWithIndex(c.position.index, Cell(value)) :: changedCells
        log.debug("New value at {} is {}", c.position.index, world(c.position.index))
      })
  }
}


object World {

  case class WorldInfo(width: Int, height: Int, cells: java.util.List[CellWithIndex])

  case class Cell(value: Double)

  case class CellWithIndex(index: Index, cell: Cell)

  case class Index(x: Int, y: Int)


  def apply(width: Int, height: Int) = {
    val _width = width
    val _height = height
    new World {
      val width: Int = _width
      val height: Int = _height
      val field: mutable.Map[Index, Cell] = mutable.Map.empty[Index, Cell]


      for (x <- 0 until width) {
        for (y <- 0 until height) {
          field += (Index(x, y) -> Cell(0))
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
  val field: mutable.Map[Index, Cell]
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
    field += (index -> cell)
  }

  def apply(index: Index) = {
    field(index)
  }

  def toList: List[CellWithIndex] = (for (entry <- field) yield CellWithIndex(entry._1, entry._2)).toList

  def worldInfo: WorldInfo = WorldInfo(width, height, toList)
}

object Agent {

  sealed trait AgentMessage

  case class MoveTo(newCell: CellWithIndex) extends AgentMessage

  case class ChangeValue(newValue: Double) extends AgentMessage

  case class AgentState(position: CellWithIndex)

}