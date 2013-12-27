package org.amcgala.agent

import com.typesafe.config.Config
import java.util
import org.amcgala.agent.Agent.Pheromone
import org.amcgala.agent.World.{ Index, Cell }
import scala.collection.JavaConversions._
import scala.util.Random
import mikera.math.PerlinNoise
import com.oddlabs.procedurality._
import org.amcgala.agent.World.Index
import org.amcgala.agent.World.Cell
import org.amcgala.agent.utils.BresenhamIterator

trait Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell]
}

class EmptyWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    for (x ← 0 until width) {
      for (y ← 0 until height) {
        field = field + (Index(x, y) -> Cell(1f))
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
        field = field + (Index(x, y) -> Cell(0))
      }
    }

    val polygon = config.getAnyRefList("org.amcgala.agent.simulation.world.definition.args").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]
    polygon.zipWithIndex.foreach {
      case (start, index) ⇒
        val end = polygon.get((index + 1) % polygon.size())
        BresenhamIterator.bresenham(start(0), start(1), end(0), end(1)).foreach(i ⇒ {
          field = field + (i -> Cell(1))
        })
    }

    field
  }

}

class FractalWorldMap extends Initialiser {

  private def createMap(size: Int) = {
    val seed: Int = Random.nextInt(266234)
    val features: Int = Random.nextInt(8) + 2
    val hills: Float = 0.89f

    val height: Channel = new Mountain(size, Utils.powerOf2Log2(size) - 6, 0.5f, seed).toChannel

    val voronoi: Voronoi = new Voronoi(size, features, features, 1, 1f, seed)
    val cliffs: Channel = voronoi.getDistance(-1f, 1f, 0f).brightness(1.5f).multiply(0.63f)

    height.channelAdd(cliffs)
    height.channelSubtract(voronoi.getDistance(1f, 0f, 0f).gamma(.75f).flipV.rotate(90))
    height.perturb(new Midpoint(size, 2, 0.1f, seed).toChannel, 0.15f)
    height.erode((24f - hills * 12f) / size, 5)

    height.smooth(1)

    height
  }

  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]

    val map = createMap(512)

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        field = field + (Index(x, y) -> Cell(map.getPixelWrap(x, y)))
      }
    }

    field
  }
}

class GradientWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    val wX = 2
    val wY = 2

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        val v = (1 + math.sin(x / wX) * math.sin(y / wY)) / 2
        field = field + (Index(x, y) -> Cell(v.toFloat))
      }
    }

    field
  }
}

class GaussWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    val x0 = width / 2
    val y0 = height / 2
    val sx = 0.01f
    val sy = 0.05f

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        val tx = math.pow(x - x0, 2) / 2 * math.pow(sx, 2)
        val ty = math.pow(y - y0, 2) / 2 * math.pow(sy, 2)
        val t = math.exp(-(tx + ty))

        field = field + (Index(x, y) -> Cell(t.toFloat))
      }
    }
    field
  }
}

class SineCosineWorldMap extends Initialiser {
  def initField(width: Int, height: Int, neighbours: List[Index], config: Config): Map[Index, Cell] = {
    import math._
    var field: Map[Index, Cell] = Map.empty[Index, Cell]
    val x0 = width / 2
    val y0 = height / 2
    val sx = 0.01f
    val sy = 0.01f
    val p = new PerlinNoise(42)

    for (x ← 0 until width) {
      for (y ← 0 until height) {
        val v = .7f * sin(3 * math.Pi / width * x) * cos(math.Pi / height * y) + .5f

        field = field + (Index(x, y) -> Cell(v.toFloat))
      }
    }

    field
  }
}

