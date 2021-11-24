package io.toolisticon.lib.krid.model

/**
 * Represents coordinates and value of a cell in a krid.
 */
data class CellValue<E>(
  /**
   * x coordinate
   */
  val x: Int,
  /**
   * y coordinate
   */
  val y: Int,
  /**
   * value
   */
  val value: E
) {

  /**
   * Create from cell and value.
   */
  constructor(cell: Cell, value: E) : this(cell.x, cell.y, value)

  /**
   * Create from pair and value.
   */
  constructor(pair: Pair<Int, Int>, value: E) : this(pair.toCell(), value = value)

  /**
   * Cell value for x/y coordinates.
   */
  val cell = Cell(x, y)

  /**
   * Move x/y coordinates by cell.
   */
  operator fun plus(cell: Cell): CellValue<E> = (this.cell + cell).let {
    copy(x = it.x, y = it.y)
  }
}

/**
 * Extract list of cells from [CellValue]s (dropping values).
 */
val <E> List<CellValue<E>>.cells: List<Cell> get() = map { it.cell }
