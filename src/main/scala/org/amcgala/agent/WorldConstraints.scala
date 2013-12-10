package org.amcgala.agent

import org.amcgala.agent.World.{ CellWithIndex, InformationObject }
import org.amcgala.agent.Agent.{ OwnerPheromone, Pheromone, AgentStates }
import akka.actor.{ PoisonPill, ActorRef }

/**
  * Ein WorldContraintsChecker überprüft während der Simulation die Aktionen der Agenten und kann entscheiden, ob eine Aktion in
  * der Simulation von einem Agenten ausgeführt werden kann.
  * Hierfür überprüft die Simulation die Anfrage des Agenten mithilfe des ConstraintsChecker und führt eine Aktion aus, kann diese
  * aber auch zurückweisen.
  */
trait WorldConstraintsChecker {
  /**
    * Überprüft, ob der Agent vom aktuellen Feld auf das Zielfeld gehen kann.
    * @param current das aktuelle Feld, auf dem der Agent steht
    * @param target das Zielfeld
    * @return true, wenn der Agent auf das Feld wechseln kann
    */
  def checkMove(requester: ActorRef, current: CellWithIndex, target: CellWithIndex, states: List[AgentStates]): Boolean

  /**
    * Überprüft, ob der Agent ein Pheromon ausschütten darf.
    * @param agent Der Zustand des Agenten
    * @param pheromone das Pheromon, das der Agent ausschütten möchte
    * @return true, wenn der Agent das Pheromon abgeben darf
    */
  def checkPheromone(agent: AgentStates, pheromone: Pheromone): Boolean

  /**
    * Überprüft, ob der Agent den Wert eines Feldes verändern darf.
    * @param currentValue der aktuelle Wert des Feldes
    * @param newValue der neue Wert, auf den der Agent den Wert ändern möchte
    * @return true, wenn der Agent den Wert verändern darf
    */
  def checkValueChange(currentValue: Double, newValue: Double): Boolean

  /**
    * Prueft, ob ein [[org.amcgala.agent.World.InformationObject]] der Zelle hinzugefuegt werden darf.
    * @param agent
    * @param informationObject
    * @return
    */
  def checkInformationObject(agent: AgentStates, informationObject: InformationObject): Boolean
}

/**
  * Standardbeschraenkungen der Simulation.
  *
  */
class RaindropConstraints extends WorldConstraintsChecker {

  def checkMove(requester: ActorRef, current: CellWithIndex, target: CellWithIndex, states: List[AgentStates]): Boolean = {
    if (states.exists(entry ⇒ entry.position == target.index)) {
      requester ! PoisonPill
      false
    } else {
      val dx = math.abs(current.index.x - target.index.x)
      val dy = math.abs(current.index.y - target.index.y)
      if (dx > 1 || dy > 1) {
        requester ! PoisonPill
        return false
      }
      true
    }
  }

  def checkPheromone(agent: AgentStates, pheromone: Pheromone): Boolean = pheromone match {
    case owner: OwnerPheromone ⇒ agent.id == owner.id
    case _                     ⇒ true
  }

  def checkValueChange(oldValue: Double, newValue: Double): Boolean = newValue >= 0 && newValue <= 1

  def checkInformationObject(agent: AgentStates, informationObject: InformationObject): Boolean = true
}

class DefaultConstraints extends WorldConstraintsChecker {

  /**
    * Überprüft, ob der Agent vom aktuellen Feld auf das Zielfeld gehen kann.
    * @param current das aktuelle Feld, auf dem der Agent steht
    * @param target das Zielfeld
    * @return true, wenn der Agent auf das Feld wechseln kann
    */
  def checkMove(requester: ActorRef, current: CellWithIndex, target: CellWithIndex, states: List[AgentStates]): Boolean = true

  /**
    * Überprüft, ob der Agent ein Pheromon ausschütten darf.
    * @param agent Der Zustand des Agenten
    * @param pheromone das Pheromon, das der Agent ausschütten möchte
    * @return true, wenn der Agent das Pheromon abgeben darf
    */
  def checkPheromone(agent: AgentStates, pheromone: Pheromone): Boolean = true

  /**
    * Überprüft, ob der Agent den Wert eines Feldes verändern darf.
    * @param currentValue der aktuelle Wert des Feldes
    * @param newValue der neue Wert, auf den der Agent den Wert ändern möchte
    * @return true, wenn der Agent den Wert verändern darf
    */
  def checkValueChange(currentValue: Double, newValue: Double): Boolean = true

  /**
    * Prueft, ob ein [[org.amcgala.agent.World.InformationObject]] der Zelle hinzugefuegt werden darf.
    * @param agent
    * @param informationObject
    * @return
    */
  def checkInformationObject(agent: Agent.AgentStates, informationObject: World.InformationObject): Boolean = true
}
