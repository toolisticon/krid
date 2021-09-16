package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell

/**
 * Transformation of a cell to another cell, taking [Steps] given directions.
 */
sealed interface StepFn : (Cell) -> Cell {
  operator fun plus(other: StepFn): Steps
}

/**
 * Take a number of steps in the given direction.
 */
data class Step(val direction: Direction, val number: Int) : StepFn {

  override fun invoke(start: Cell): Cell = direction.fn(start, number)

  override operator fun plus(other: StepFn): Steps = when (other) {
    is Direction -> this + other.singleStep
    is Step -> Steps(listOf(this, other))
    is Steps -> Steps(this) + other
  }

  override fun toString(): String = "$direction($number)"
}

/**
 * A sequence of steps to take.
 */
data class Steps(private val list: List<Step>) : StepFn {
  constructor(step: Step) : this(listOf(step))

  override operator fun plus(other: StepFn): Steps = when (other) {
    is Direction -> plus(other.singleStep)
    is Step -> copy(list = list + other)
    is Steps -> copy(list = list + other.list)
  }

  /**
   * Returns a sequence of cells reached by the single steps.
   */
  fun walk(start:Cell) : Sequence<Cell> = sequence {
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

enum class Direction(val fn: (Cell, Int) -> Cell) : StepFn {
  UP(fn = { start, num -> cell(start.x, start.y - num) }),
  UP_RIGHT(fn = { start, num -> cell(start.x + num, start.y - num) }),
  RIGHT(fn = { start, num -> cell(start.x + num, start.y) }),
  DOWN_RIGHT(fn = { start, num -> cell(start.x + num, start.y + num) }),
  DOWN(fn = { start, num -> cell(start.x, start.y + num) }),
  DOWN_LEFT(fn = { start, num -> cell(start.x - num, start.y + num) }),
  LEFT(fn = { start, num -> cell(start.x - num, start.y) }),
  UP_LEFT(fn = { start, num -> cell(start.x - num, start.y - num) }),
  ;

  val singleStep = this(1)

  override operator fun plus(other: StepFn): Steps = when (other) {
    is Direction -> singleStep + other.singleStep
    is Step, is Steps -> singleStep + other
  }

  /**
   * Sequence of cells in given direction.
   */
  fun beam(cell: Cell, includeStart: Boolean = false): Sequence<Cell> = generateSequence(if (includeStart) cell else singleStep(cell), singleStep::invoke)

  fun Cell.beam(direction: Direction, includeStart: Boolean = false) = direction.beam(this, includeStart)
  fun Cell.take(direction: Direction, num: Int, includeStart: Boolean = false) = beam(direction, includeStart).take(num).toList()

  override fun invoke(cell: Cell) = singleStep(cell)
  operator fun invoke(number: Int): Step = Step(this, number)
}
