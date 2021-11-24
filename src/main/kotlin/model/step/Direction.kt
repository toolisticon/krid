package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.Cell

enum class Direction(val fn: (Cell, Int) -> Cell) : StepFn {
  UP(fn = { start, num -> start + cell(0, -num) }),
  UP_RIGHT(fn = { start, num -> start + cell(num, -num) }),
  RIGHT(fn = { start, num -> start + cell(num, 0) }),
  DOWN_RIGHT(fn = { start, num -> start + cell(num, num) }),
  DOWN(fn = { start, num -> start + cell(0, num) }),
  DOWN_LEFT(fn = { start, num -> start + cell(-num, num) }),
  LEFT(fn = { start, num -> start + cell(-num, 0) }),
  UP_LEFT(fn = { start, num -> start + cell(-num, -num) }),
  NONE(fn = { start, _ -> start }),
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
    else -> TODO()
  }

  /**
   * Sequence of cells in given direction.
   */
  fun beam(cell: Cell, includeStart: Boolean = false): Sequence<Cell> = generateSequence(if (includeStart) cell else singleStep(cell), singleStep::invoke)

  fun Cell.beam(direction: Direction, includeStart: Boolean = false) = direction.beam(this, includeStart)
  fun Cell.take(direction: Direction, num: Int, includeStart: Boolean = false) = beam(direction, includeStart).take(num).toList()

  override fun invoke(start: Cell) = singleStep(start)

  operator fun invoke(number: Int) = DirectionStep(this, number)
}
