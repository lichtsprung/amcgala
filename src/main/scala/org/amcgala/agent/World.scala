package org.amcgala.agent

import org.amcgala.agent.Agent.Pheromone
import com.typesafe.config.Config
import java.util
import scala.util.Random
import org.amcgala.agent.World._
import org.amcgala.agent.World.CellWithIndex
import org.amcgala.agent.World.Index
import org.amcgala.agent.World.Cell
import scala.collection.JavaConversions._

object World {

  trait InformationObject extends Message

  case class QuantisationError(value: Float) extends InformationObject

  case object Visited extends InformationObject

  type PheromoneMap = Map[Pheromone, Float]

  case class WorldInfo(width: Int, height: Int, cells: java.util.List[(Index, Cell)])

  case class Cell(value: Float,
                  pheromones: PheromoneMap = Map.empty[Pheromone, Float],
                  informationObjects: List[InformationObject] = List.empty[InformationObject]) extends Message

  case class JCell(value: Float,
                   pheromones: util.Map[Pheromone, Float] = new util.HashMap[Pheromone, Float](),
                   informationObjects: util.List[InformationObject] = new util.ArrayList[InformationObject]()) extends Message

  case class NeighbourCellWithIndex(relativeIndex: Index, absoluteIndex: Index, cell: Cell) extends Message

  case class CellWithIndex(index: Index, cell: Cell) extends Message

  case class JCellWithIndex(index: Index, cell: JCell) extends Message

  case class Index(x: Int, y: Int) extends Message

  val RandomIndex = Index(-1, -1)

  def apply(config: Config) = {
    val _width = config.getInt("org.amcgala.agent.simulation.world.width")
    val _height = config.getInt("org.amcgala.agent.simulation.world.height")
    val worldDef = config.getString("org.amcgala.agent.simulation.world.definition.class")

    val cl = ClassLoader.getSystemClassLoader.loadClass(worldDef)
    new World {
      val initialiser = cl.newInstance().asInstanceOf[Initialiser]
      val width: Int = _width
      val height: Int = _height

      val neighbours: List[Index] = {
        val lists = config.getAnyRefList("org.amcgala.agent.simulation.world.neighbours").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]
        (for (
          list ← lists
        ) yield Index(list(0), list(1))).toList
      }

      var field: Map[Index, Cell] = initialiser.initField(width, height, neighbours, config)
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
    println(i)

    CellWithIndex(i, field(i))
  }

  def neighbours(index: Index): Map[Index, CellWithIndex] = {
    var neighbourCells = Map.empty[Index, CellWithIndex]

    neighbours.zipWithIndex.foreach {
      case (value, i) ⇒
        val ix = (((index.x + value.x) % width) + width) % width
        val iy = (((index.y + value.y) % height) + height) % height
        val nx = Index(ix, iy)

        neighbourCells = neighbourCells + (value -> CellWithIndex(nx, field(nx)))
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

  def addInformationObject(index: Index, informationObject: InformationObject) {
    val c = field(index)
    field = field + (index -> Cell(c.value, c.pheromones, informationObject :: c.informationObjects))
  }

  def apply(index: Index) = {
    field(index)
  }

  def toList: List[CellWithIndex] = (for (entry ← field) yield CellWithIndex(entry._1, entry._2)).toList

  def worldInfo: WorldInfo = WorldInfo(width, height, field.toList)

  def update(): Unit = {
    var newField = Map.empty[Index, Cell]

    field map {
      e ⇒
        val n = neighbours(e._1) // neighbours of current cell
        var currentCellPheromones = newField.getOrElse(e._1, Cell(0, Map.empty[Pheromone, Float])).pheromones // already updated pheromone values
        val currentCellValue = field(e._1).value // value of current cell

        e._2.pheromones map {
          p ⇒
            val decay = p._2 * p._1.decayRate // new value of this pheromone after decay
            val sum = decay + currentCellPheromones.getOrElse(p._1, 0.0f) // sum of values (this cell + this pheromone spread from neighbour cells)
            if (sum > 0.009) {
              currentCellPheromones = currentCellPheromones + (p._1 -> math.min(1f, sum))
            }
            val spread = p._2 * p._1.spreadRate
            n map {
              neighbour ⇒
                val neighbourCell = newField.getOrElse(e._1, Cell(field(neighbour._1).value, Map.empty[Pheromone, Float]))
                var neighbourPheromones = neighbourCell.pheromones
                val sum = spread + neighbourPheromones.getOrElse(p._1, 0.0f)
                if (sum > 0.009) {
                  neighbourPheromones = neighbourPheromones + (p._1 -> math.min(1f, sum))
                }
                newField = newField + (neighbour._1 -> Cell(field(neighbour._1).value, neighbourPheromones))
            }
        }
        newField = newField + (e._1 -> Cell(currentCellValue, currentCellPheromones))
    }
    field = newField
  }
}

trait Direction extends Message {
  def relativeIndex: Index
}

object Directions {
  val LEFT = new Direction {
    def relativeIndex: Index = Index(-1, 0)
  }

  val RIGHT = new Direction {
    def relativeIndex: Index = Index(1, 0)
  }

  val UP = new Direction {
    def relativeIndex: Index = Index(0, -1)
  }

  val DOWN = new Direction {
    def relativeIndex: Index = Index(0, 1)
  }

  val UP_LEFT = new Direction {
    def relativeIndex: Index = Index(-1, -1)
  }

  val UP_RIGHT = new Direction {
    def relativeIndex: Index = Index(1, -1)
  }

  val DOWN_LEFT = new Direction {
    def relativeIndex: Index = Index(-1, 1)
  }

  val DOWN_RIGHT = new Direction {
    def relativeIndex: Index = Index(1, 1)
  }
}