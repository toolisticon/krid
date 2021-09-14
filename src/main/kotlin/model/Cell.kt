package io.toolisticon.lib.krid.model

/**
 * A Cell represents a (x,y)-coordinate inside a Krid.
 */
data class Cell(val x: Int, val y: Int) : Comparable<Cell> {

  operator fun plus(other: Cell): Cell = copy(x = x + other.x, y = y + other.y)

  operator fun minus(other: Cell): Cell = copy(x = x - other.x, y = y - other.y)

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
}
