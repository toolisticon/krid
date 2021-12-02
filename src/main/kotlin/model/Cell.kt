package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.StepFn
import java.util.*

interface Coordinates {
  val x: Int
  val y: Int
}

/**
 * A Cell represents a (x,y)-coordinate inside a Krid.
 */
data class Cell(override val x: Int, override val y: Int) : Comparable<Cell>, Coordinates {
  companion object {
    /**
     * @throws IllegalArgumentException if first is not >= second
     */
    fun requireGreaterThanOrEqual(first: Cell, second: Cell) = require(first >= second) {
      "$first has to be right and/or down from $second."
    }
  }

  /**
   * List of orthogonal adjacent cells, starting with 12° clock (`UP`).
   */
  val orthogonalAdjacent: List<Cell> by lazy {
    adjacent(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
  }

  /**
   * List of adjacent cells, starting with 12° clock (`UP`).
   */
  val adjacent: List<Cell> by lazy {
    adjacent(*Direction.ADJACENT)
  }

  /**
   * Return the cell you get when you take the given step.
   */
  operator fun invoke(step: StepFn): Cell = step(this)

  /**
   * Create a new cell with coordinates `x=c1.x + c2.x, y=c1.y + c2.y`.
   */
  operator fun plus(other: Cell): Cell = copy(x = x + other.x, y = y + other.y)

  /**
   * Create a new cell with coordinates `x=c1.x - c2.x, y=c1.y - c2.y`.
   */
  operator fun minus(other: Cell): Cell {
    require(this >= other) { "$this has to be >= $other." }
    return copy(x = x - other.x, y = y - other.y)
  }

  /**
   * A cell is LT when it is up and/or left, RT when it is down and/or right, EQ if same
   * ```
   * 00 01
   * 10 11
   * ```
   *
   * * 00 EQ 00; 00 LT 01,10,11
   * * 01 GT 00; 01 EQ 01; 01 LT 00,10
   * * 10 EQ 10; 10 GT 00; 10 LT 01,11
   * * 11 EQ 11; 11 GT 00,01,10
   *
   * return `0` if EQ, `-1` if LT, `+1` if GT
   */
  override fun compareTo(other: Cell): Int = when {
    this == other -> 0
    (x > other.x && y >= other.y) ||
      (y > other.y && x >= other.x) -> +1
    else -> -1
  }

  operator fun times(number: Int): Cell = copy(x = x * number, y = y * number)

  private fun adjacent(vararg directions: Direction): List<Cell> = directions.map { this(it.singleStep) }
}

/**
 * Transforms a pair of [Int]s to a type safe [Cell].
 */
fun Pair<Int, Int>.toCell() = cell(first, second)

/**
 * Transforms a list of pairs to list of cells.
 */
fun List<Pair<Int, Int>>.toCells() = map { it.toCell() }


