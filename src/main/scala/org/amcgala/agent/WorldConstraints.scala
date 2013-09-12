package org.amcgala.agent

import org.amcgala.agent.World.Cell
import org.amcgala.agent.Agent.{OwnerPheromone, Pheromone, AgentState}

trait WorldConstraintsChecker {
  def checkMove(current: Cell, target: Cell): Boolean

  def checkPheromone(agent: AgentState, pheromone: Pheromone): Boolean

  def checkValueChange(oldValue: Double, newValue: Double): Boolean
}

class DefaultConstraints extends WorldConstraintsChecker {
  def checkMove(current: Cell, target: Cell): Boolean = true

  def checkPheromone(agent: AgentState, pheromone: Pheromone): Boolean = pheromone match {
    case owner: OwnerPheromone => agent.id == owner.id
    case _ => true
  }

  def checkValueChange(oldValue: Double, newValue: Double): Boolean = newValue >= 0 && newValue <= 1
}