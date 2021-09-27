package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.fn.IndexTransformer
import io.toolisticon.lib.krid.model.*

/**
 * Common parent class for [Krid] and [ExpendableKrid].
 */
abstract class AbstractKrid<E> {

  /**
   * width/height dimension of this krid.
   */
  abstract val dimension: Dimension

  /**
   * A value of type `<E>` that  represents an empty [Cell]. In case the krid type is `<E?>`, this might be null.
   */
  abstract val emptyElement: E

  /**
   * Internal storage via list, visible only to subclasses.
   */
  protected abstract val list: List<E>

  /**
   * Helper to align cell coordinates and list index.
   */
  protected val indexTransformer by lazy { IndexTransformer(dimension.width) }

  /**
   * Checks if the given row-index is in current [Dimension.rowRange].
   *
   * @param rowIndex the row index to check
   * @throws IllegalArgumentException if not in range
   * @return the row index if in range
   */
  protected fun requireInRows(rowIndex: Int): Int = rowIndex.apply { require(rowIndex in dimension.rowRange) { "$this has to be in ${dimension.rowRange}." } }

  /**
   * Checks if the given column-index is in current [Dimension.columnRange].
   *
   * @param columnIndex the column index to check
   * @throws IllegalArgumentException if not in range
   * @return the column index if in range
   */
  protected fun requireInColumns(columnIndex: Int): Int =
    columnIndex.apply { require(this in dimension.columnRange) { "$this has to be in ${dimension.columnRange}." } }

  /**
   * @return `true` if all cell values in this krid are the [emptyElement].
   */
  fun isEmpty(): Boolean = list.all { isEmptyElement(it) }

  /**
   * Get value at given coordinates.
   *
   * @param x x coordinate of cell (has to be in bound)
   * @param y y coordinate of cell (has to be in bound)
   * @return value at given coordinates.
   */
  operator fun get(x: Int, y: Int): E = list[indexTransformer.toIndex(requireInColumns(x), requireInRows(y))]

  /**
   * @return the rows of this krid.
   */
  fun rows(): Rows<E> = Rows(dimension.rowRange.map(this::row))

  /**
   * @return the columns of this krid.
   */
  fun columns(): Columns<E> = Columns(dimension.columnRange.map(this::column))

  /**
   * @param index which row to get
   * @return row with index.
   */
  abstract fun row(index: Int): Row<E>

  /**
   * @param index which column to get
   * @return column with index.
   */
  abstract fun column(index: Int): Column<E>

  /**
   * Iterate row by row, starting to left to bottom right.
   *
   * @return iterator of [CellValue]s.
   */
  fun iterator(): Iterator<CellValue<E>> = list
    .mapIndexed { index, e -> cell(indexTransformer.toCell(index), e) }
    .iterator()

  /**
   * Returns direct neighbors up, left, rdown and right of given cell.
   *
   * @param cell the base cell from which to check neighbors
   * @return list of cells up, left, down, right (in bounds)
   */
  fun orthogonalAdjacentCells(cell: Cell): List<Cell> = dimension.filterInBounds(cell.orthogonalAdjacent)

  /**
   * Returns direct neighbors in all 8 directions of given cell.
   *
   * @param cell the base cell from which to check neighbors
   * @return list of cells in all 8 directions (in bounds)
   */
  fun adjacentCells(cell: Cell): List<Cell> = dimension.filterInBounds(cell.adjacent)
}


/**
 * Sequence of [iterator].
 *
 * @see iterator
 * @return sequence of [CellValue]s.
 */
fun <E> AbstractKrid<E>.sequence(): Sequence<CellValue<E>> = iterator().asSequence()

/**
 * Convenient extension for [AbstractKrid.orthogonalAdjacentCells].
 *
 * @see AbstractKrid.orthogonalAdjacentCells
 */
fun <E> AbstractKrid<E>.orthogonalAdjacentCells(x: Int, y: Int) = orthogonalAdjacentCells(cell(x, y))

/**
 * Convenient extension for [AbstractKrid.adjacentCells].
 *
 * @see AbstractKrid.adjacentCells
 */
fun <E> AbstractKrid<E>.adjacentCells(x: Int, y: Int) = adjacentCells(cell(x, y))

/**
 * `true` if a value is equal to the defined [AbstractKrid.emptyElement].
 */
internal val <E>AbstractKrid<E>.isEmptyElement: (E) -> Boolean get() = { it == emptyElement }

/**
 * Convenience extension for [AbstractKrid.get].
 */
operator fun <E> AbstractKrid<E>.get(cell: Cell) = get(cell.x, cell.y)

/**
 * Gets multiple [CellValue]s for given list of [Cell]s.
 *
 * @see [AbstractKrid.get]
 */
operator fun <E> AbstractKrid<E>.get(cells: List<Cell>): List<CellValue<E>> {
  dimension.filterNotInBounds(cells).also {
    require(it.isEmpty()) { "Cannot get() because some cells are out of bounds: $it." }
  }

  return cells.map { cell(it, get(it)) }
}

/**
 * Prints the [Krid] to console by converting the cell contents
 * to char using given function.
 *
 * Defaults to "first char", so `Boolean.TRUE` becomes `t`.
 * `null` ist represented as `.`.
 */
fun <E> AbstractKrid<E>.ascii(toChar: (E) -> Char? = { it?.toString()?.first() ?: '.' }): String = this.rows().joinToString(separator = "\n") {
  it.map { e -> toChar(e) ?: '.' }.joinToString(separator = "")
}
