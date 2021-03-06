package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.model.Cell
import kotlin.math.absoluteValue

/**
 * Take a number of steps in the given direction.
 */
data class DirectionStep(val direction: Direction, val number: Int) : StepFn {
  companion object {
    private val regex = """(\w+)\((\d+)\)""".toRegex()

    fun horizontal(number: Int): DirectionStep = when {
      number < 0 -> Direction.LEFT(number.absoluteValue)
      number > 0 -> Direction.RIGHT(number.absoluteValue)
      else -> Direction.NONE(0)
    }

    fun vertical(number: Int): DirectionStep = when {
      number < 0 -> Direction.UP(number.absoluteValue)
      number > 0 -> Direction.DOWN(number.absoluteValue)
      else -> Direction.NONE(0)
    }

    fun parse(string: String): DirectionStep {
      val (directionName, numberValue) = requireNotNull(regex.find(string)).destructured
      return Direction.valueOf(directionName.trim()).invoke(numberValue.trim().toInt())
    }

  }

  private val fn : (Cell,Int) -> Cell by lazy {
    { start, num ->
      (CoordinatesStep(direction.coordinates) * num).invoke(start)
    }
  }

  override fun invoke(start: Cell): Cell = fn(start, number)

  override operator fun plus(other: StepFn): CompositeStep = when (other) {
    is Direction -> this + other.singleStep
    is DirectionStep -> CompositeStep(listOf(this, other))
    is CompositeStep -> CompositeStep(this) + other
    is CoordinatesStep -> this + other.directionSteps
  }

  override fun times(number: Int): StepFn {
    val newNumber = this.number * number
    return if (newNumber > 0) {
      copy(number = newNumber)
    } else if (newNumber < 0) {
      direction.opposite(-newNumber)
    } else {
      Direction.NONE(0)
    }
  }

  override fun toString(): String = "$direction($number)"
}

val DirectionStep.opposite get() = copy(direction = direction.opposite, number = number)
