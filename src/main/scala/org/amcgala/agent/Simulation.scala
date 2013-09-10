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
import com.typesafe.config.{ConfigFactory, Config}
import java.util


object Simulation {

  case class SimulationConfig(config: Config)

  case class SimulationUpdate(currentState: AgentState, neighbours: Array[CellWithIndex])

  case class SimulationState(worldInfo: WorldInfo, agents: java.util.List[AgentState])

  case class SimulationStateUpdate(changedCells: java.util.List[(Index, Cell)], agents: java.util.List[AgentState])

  case class Register(index: Index)

  case object RegisterWithRandomIndex

  case object RegisterStateLogger

  case object RegisterWithDefaultIndex

  case object Update

  def props(): Props = Props(classOf[Simulation])

}


class Simulation extends Actor with ActorLogging {
  val defaultConfig = ConfigFactory.load("simulation")

  var agents = Map.empty[ActorRef, AgentState]
  var stateLogger = Set.empty[ActorRef]

  var world: Option[World] = None


  var currentConfig = ConfigFactory.empty().withFallback(defaultConfig)
  var pingTime = currentConfig.getInt("org.amcgala.agent.simulation.ping-time").milliseconds
  var defaultPosition = {
    val l = currentConfig.getIntList("org.amcgala.agent.default-position")
    assert(l.size() == 2)
    World.Index(l(0), l(1))
  }

  def receive: Actor.Receive = waitForWorld

  def waitForWorld: Actor.Receive = {
    case SimulationConfig(config) =>
      currentConfig = config.withFallback(defaultConfig)
      world = Some(World(currentConfig))
      context.system.scheduler.schedule(5 seconds, pingTime, self, Update)
      context.become(simulationLifeCycle orElse handleAgentMessages)
  }

  def simulationLifeCycle: Actor.Receive = {
    case RegisterWithRandomIndex =>
      world map (w => {
        val randomCell = w.randomCell
        agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), randomCell.index, randomCell.cell))
        log.debug("Registering new Actor at {}", randomCell)
        log.debug("Agents on this cell: {}", agents.filter(entry => entry._2 == randomCell).keySet)
      })

    case Register(index) =>
      world map (w => {
        agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index, w(index)))
      })

    case RegisterWithDefaultIndex =>
      defaultPosition match {
        case World.RandomIndex =>
          self forward RegisterWithRandomIndex
        case index: Index =>
          world map (w => {
            agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index, w(index)))
            log.info("Registering new Actor at {}", index)
          })
      }

    case RegisterStateLogger if !agents.exists(e => e._1 == sender) =>
      world map (w => {
        sender ! SimulationState(w.worldInfo, agents.values.toList)
        stateLogger = stateLogger + sender
        log.info(s"Registered StateLoggers: $stateLogger")
      })

    case Update =>
      world map (w => {
        log.debug(s"Number of agents ${agents.size}")
        if (currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")) {
          w.update()
        }
        agents map {
          case (ref, currentState) => {
            val neighbourCells = w.neighbours(currentState.position)
            ref ! SimulationUpdate(currentState, neighbourCells)
          }
        }
        stateLogger map (logger => {
          log.debug(s"Sending SimulationStateUpdate to $logger")
          logger ! SimulationStateUpdate(w.field.toList, agents.values.toList)
        })
      })

  }

  def handleAgentMessages: Actor.Receive = {
    case MoveTo(index) =>
      world map (w => {
        log.debug("Moving agent {} to {}", sender, index)
        agents = agents + (sender -> AgentState(AgentID(sender.hashCode()), index, w(index)))
      })

    case ReleasePheromone(pheromone) =>
      world map (w => {
        if (currentConfig.getBoolean("org.amcgala.agent.simulation.world.pheromones")) {
          agents.get(sender) map (i => w.addPheromone(i.position, pheromone))
        }
      })

    case Death =>
      agents = agents - sender

    case ChangeValue(value) =>
      world map (w => {
        agents.get(sender) map (c => {
          w.change(c.position, value)
        })
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


  def apply(config: Config) = {
    val _width = config.getInt("org.amcgala.agent.simulation.world.width")
    val _height = config.getInt("org.amcgala.agent.simulation.world.height")
    val worldDef = config.getString("org.amcgala.agent.simulation.world.worldDefinition")
    println(s"Loading $worldDef")
    val cl = ClassLoader.getSystemClassLoader.loadClass(worldDef)
    new World {
      val initialiser = cl.newInstance().asInstanceOf[Initialiser]
      val width: Int = _width
      val height: Int = _height
      var field: Map[Index, Cell] = initialiser.initField(width, height, neighbours, config)

      val neighbours: List[Index] = {
        import scala.collection.JavaConversions._
        val lists = config.getAnyRefList("org.amcgala.agent.simulation.world.neighbours").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]
        (for (
          list <- lists
        ) yield Index(list(0), list(1))).toList
      }
    }
  }
}

trait Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell]
}


class EmptyWorldMapInitialiser extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    for (x <- 0 until width) {
      for (y <- 0 until height) {
        field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
      }
    }
    field
  }
}

class PolygonWorldMapInitialiser extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]

    for (x <- 0 until width) {
      for (y <- 0 until height) {
        field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
      }
    }

    val polygon = config.getAnyRefList("org.amcgala.shape.polygon").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]
    polygon.zipWithIndex.foreach {
      case (start, index) =>
        val end = polygon.get((index + 1) % polygon.size())
        bresenham(start(0), start(1), end(0), end(1)).foreach(i => {
          field = field + (i -> Cell(1, Map.empty[Pheromone, Float]))
        })
    }

    field
  }


  def bresenham(x0: Int, y0: Int, x1: Int, y1: Int) = {
    import scala.math.abs

    val dx = abs(x1 - x0)
    val dy = abs(y1 - y0)

    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1

    new Iterator[Index] {
      var (x, y) = (x0, y0)
      var err = dx - dy

      def next = {
        val omitted = Index(x, y)
        val e2 = 2 * err
        if (e2 > -dy) {
          err -= dy
          x += sx
        }
        if (e2 < dx) {
          err += dx
          y += sy
        }
        omitted
      }

      def hasNext = (!(x == x1 && y == y1))
    }
  }


}


trait World {
  val width: Int
  val height: Int
  val neighbours: List[Index]
  var field: Map[Index, Cell]

  def randomCell: CellWithIndex = {
    val col = Random.nextInt(width)
    val row = Random.nextInt(height)
    val i = Index(col, row)

    CellWithIndex(i, field(i))
  }

  def neighbours(index: Index): Array[CellWithIndex] = {
    val neighbourCells = Array.ofDim[CellWithIndex](neighbours.length)

    neighbours.zipWithIndex.foreach {
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

  case class SpawnAt(position: Index) extends AgentMessage

  case class MoveTo(index: Index) extends AgentMessage

  case class ChangeValue(newValue: Float) extends AgentMessage

  case class ReleasePheromone(pheromone: Pheromone) extends AgentMessage

  case object Death extends AgentMessage

  case class AgentState(id: AgentID, position: Index, cell: Cell)

}