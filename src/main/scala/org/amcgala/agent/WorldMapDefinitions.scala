package org.amcgala.agent

import com.typesafe.config.Config
import java.util
import org.amcgala.agent.Agent.Pheromone
import org.amcgala.agent.World.{ Index, Cell }
import scala.collection.JavaConversions._
import scala.util.Random

trait Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell]
}

class EmptyWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    for (x ← 0 until width) {
      for (y ← 0 until height) {
        field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
      }
    }
    field
  }
}

class PolygonWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
      }
    }

    val polygon = config.getAnyRefList("org.amcgala.agent.simulation.world.definition.args").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]
    polygon.zipWithIndex.foreach {
      case (start, index) ⇒
        val end = polygon.get((index + 1) % polygon.size())
        bresenham(start(0), start(1), end(0), end(1)).foreach(i ⇒ {
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
        val point = Index(x, y)
        val e2 = 2 * err
        if (e2 > -dy) {
          err -= dy
          x += sx
        }
        if (e2 < dx) {
          err += dx
          y += sy
        }
        point
      }

      def hasNext = !(x == x1 && y == y1)
    }
  }
}

class FractalWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        // TODO Noise Function
        field = field + (Index(x, y) -> Cell(0, Map.empty[Pheromone, Float]))
      }
    }

    field
  }
}

class GradientWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]

    val vertical = Random.nextBoolean()

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        vertical match {
          case true ⇒
            val yStep = 1.0f / height
            field = field + (Index(x, y) -> Cell(yStep * y, Map.empty[Pheromone, Float]))
          case false ⇒
            val xStep = 1.0f / width
            field = field + (Index(x, y) -> Cell(xStep * x, Map.empty[Pheromone, Float]))
        }

      }
    }

    field
  }
}