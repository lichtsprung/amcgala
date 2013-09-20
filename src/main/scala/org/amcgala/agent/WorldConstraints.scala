package org.amcgala.agent

import org.amcgala.agent.World.Cell
import org.amcgala.agent.Agent.{ OwnerPheromone, Pheromone, AgentState }

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
  def checkMove(current: Cell, target: Cell): Boolean

  /**
   * Überprüft, ob der Agent ein Pheromon ausschütten darf.
   * @param agent Der Zustand des Agenten
   * @param pheromone das Pheromon, das der Agent ausschütten möchte
   * @return true, wenn der Agent das Pheromon abgeben darf
   */
  def checkPheromone(agent: AgentState, pheromone: Pheromone): Boolean

  /**
   * Überprüft, ob der Agent den Wert eines Feldes verändern darf.
   * @param currentValue der aktuelle Wert des Feldes
   * @param newValue der neue Wert, auf den der Agent den Wert ändern möchte
   * @return true, wenn der Agent den Wert verändern darf
   */
  def checkValueChange(currentValue: Double, newValue: Double): Boolean
}

class DefaultConstraints extends WorldConstraintsChecker {
  def checkMove(current: Cell, target: Cell): Boolean = true

  def checkPheromone(agent: AgentState, pheromone: Pheromone): Boolean = pheromone match {
    case owner: OwnerPheromone ⇒ agent.id == owner.id
    case _                     ⇒ true
  }

  def checkValueChange(oldValue: Double, newValue: Double): Boolean = newValue >= 0 && newValue <= 1
}