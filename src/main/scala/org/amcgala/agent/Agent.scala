package org.amcgala.agent

import org.amcgala.agent.Agent.Pheromone
import org.amcgala.agent.World.{ Index, InformationObject }

object Agent {

  trait Pheromone {
    val strength: Float
    val decayRate: Float
    val spreadRate: Float
  }

  case class OwnerPheromone(id: AgentID, strength: Float = 1f, decayRate: Float = 0.66f, spreadRate: Float = 0.09f) extends Pheromone

  case class AgentID(id: Int)

  case class AgentState(id: AgentID, position: Index)

}

object AgentMessages {

  sealed trait AgentMessage

  case class SpawnAt(position: Index) extends AgentMessage

  case object SpawnRejected extends AgentMessage

  case class MoveTo(index: Index) extends AgentMessage

  case class ChangeValue(newValue: Float) extends AgentMessage

  case class PutInformationObject(informationObject: InformationObject) extends AgentMessage

  case class PutInformationObjectTo(index: Index, informationObject: InformationObject) extends AgentMessage

  case class ReleasePheromone(pheromone: Pheromone) extends AgentMessage

  case object Death extends AgentMessage

  case object Success extends AgentMessage

  case object Failure extends AgentMessage

}