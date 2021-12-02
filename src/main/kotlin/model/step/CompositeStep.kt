package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Coordinates


/**
 * A sequence of steps to take.
 */
data class CompositeStep(private val list: List<StepFn>) : StepFn, List<StepFn> by list {
  companion object {

    operator fun invoke(coordinates: Coordinates, optimize: Boolean = false): CompositeStep {
      // dont care when all is zero
      if (coordinates.x == 0 && coordinates.y == 0) {
        return CompositeStep(Direction.NONE(0))
      }

      val list = mutableListOf<DirectionStep>()
      if (optimize) {
        TODO("this will return the minimal way using diagonals")
      } else {
        if (coordinates.x != 0)
          list.add(DirectionStep.horizontal(coordinates.x))
        if (coordinates.y != 0)
          list.add(DirectionStep.vertical(coordinates.y))
      }
      return CompositeStep(list)
    }
  }

  constructor(step: StepFn) : this(listOf(step))

  override operator fun plus(other: StepFn): CompositeStep = when (other) {
    is Direction -> plus(other.singleStep)
    is DirectionStep -> copy(list = list + other)
    is CompositeStep -> copy(list = list + other.list)
    is CoordinatesStep -> this + other.directionSteps
  }

  override fun times(number: Int): StepFn = copy(list = list.map { it.times(number) })

  /**
   * Returns a sequence of cells reached by the single steps.
   */
  fun walk(start: Cell): Sequence<Cell> = sequence {
    var current = start
    yield(current)
    list.forEach {
      current = it(current)
      yield(current)
    }
  }

  override fun invoke(start: Cell): Cell = walk(start).last()

  override fun toString(): String = list.joinToString(" + ") { it.toString() }
}

