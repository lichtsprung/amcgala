package org.amcgala.agent

import akka.actor.{ ActorLogging, Props, Actor }
import org.amcgala.testing.agent.{ FloodFillAgent, BresenhamLineAgent }
import java.util

/**
 *
 */
class DrawAndFillPolygonAgent extends Actor with ActorLogging {
  val config = context.system.settings.config
  var linesDone = 0
  val polygon = config.getAnyRefList("org.amcgala.agent.client.args").asInstanceOf[util.ArrayList[util.ArrayList[Int]]]

  override def preStart(): Unit = {
    import scala.collection.JavaConversions._

    polygon.zipWithIndex.foreach {
      case (start, index) ⇒
        val end = polygon((index + 1) % polygon.size)
        context.actorOf(Props(classOf[BresenhamLineAgent], start(0), start(1), end(0), end(1)))
    }
  }

  def receive: Actor.Receive = {
    case Agent.Success ⇒
      linesDone += 1
      if (linesDone == polygon.size()) {
        context.actorOf(Props(classOf[FloodFillAgent]))
      }
  }
}
