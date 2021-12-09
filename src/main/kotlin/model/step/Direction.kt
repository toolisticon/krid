package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Coordinates
import io.toolisticon.lib.krid.model.toPair
import kotlin.math.sign

enum class Direction(
  val coordinates: Coordinates
) : StepFn {

  UP(coordinates = cell(0, -1)),
  UP_RIGHT(coordinates = cell(1, -1)),
  RIGHT(coordinates = cell(1, 0)),
  DOWN_RIGHT(coordinates = cell(1, 1)),
  DOWN(coordinates = cell(0, 1)),
  DOWN_LEFT(coordinates = cell(-1, 1)),
  LEFT(coordinates = cell(-1, 0)),
  UP_LEFT(coordinates = cell(-1, -1)),
  NONE(coordinates = cell(0, 0)),
  ;

  companion object {
    val ADJACENT: Array<Direction> = values().toList().minus(NONE).toTypedArray()

    private val OPPOSITES = mapOf(
      NONE to NONE,
      UP to DOWN,
      DOWN to UP,
      LEFT to RIGHT,
      RIGHT to LEFT,
      UP_LEFT to DOWN_RIGHT,
      DOWN_RIGHT to UP_LEFT,
      UP_RIGHT to DOWN_LEFT,
      DOWN_LEFT to UP_RIGHT,
    )

    private val BY_COORDINATES: Map<Pair<Int, Int>, Direction> = buildMap {
      values().forEach {
        put(it.coordinates.toPair(), it)
      }
    }

    fun valueOf(coordinates: Coordinates): Direction = BY_COORDINATES[cell(coordinates.x.sign, coordinates.y.sign).toPair()]!!

  }

  val singleStep by lazy {
    when (this) {
      NONE -> invoke(0)
      else -> invoke(1)
    }
  }

  val opposite: Direction by lazy { OPPOSITES[this]!! }

  override operator fun plus(other: StepFn): CompositeStep = when (other) {
    is Direction -> singleStep + other.singleStep
    is DirectionStep, is CompositeStep -> singleStep + other
    is CoordinatesStep -> this + other.directionSteps
  }

  override fun times(number: Int): DirectionStep = invoke(number)

  /**
   * Sequence of cells in given direction.
   */
  fun beam(cell: Cell, includeStart: Boolean = false): Sequence<Cell> = generateSequence(if (includeStart) cell else singleStep(cell), singleStep::invoke)

  fun Cell.beam(direction: Direction, includeStart: Boolean = false) = direction.beam(this, includeStart)
  fun Cell.take(direction: Direction, num: Int, includeStart: Boolean = false) = beam(direction, includeStart).take(num).toList()

  override fun invoke(start: Cell) = singleStep(start)

  operator fun invoke(number: Int) = DirectionStep(this, number)
}
