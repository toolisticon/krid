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


  operator fun get(x: Int, y: Int): E = list[requireInRows(y) * dimension.width + requireInColumns(x)]

  fun rows(): Rows<E> = Rows(dimension.rowRange.map(this::row))
  fun columns(): Columns<E> = Columns(dimension.columnRange.map(this::column))

  abstract fun row(index: Int): Row<E>
  abstract fun column(index: Int): Column<E>

  fun iterator(): Iterator<CellValue<E>> = list
    .mapIndexed { index, e -> cell(indexTransformer.toCell(index), e) }
    .iterator()

  fun orthogonalAdjacentCells(cell: Cell): List<Cell> = dimension.filterInBounds(cell.orthogonalAdjacent)
  fun adjacentCells(cell: Cell): List<Cell> = dimension.filterInBounds(cell.adjacent)
}

fun <E> AbstractKrid<E>.orthogonalAdjacentCells(x: Int, y: Int) = orthogonalAdjacentCells(cell(x, y))
fun <E> AbstractKrid<E>.adjacentCells(x: Int, y: Int) = adjacentCells(cell(x, y))
internal val <E>AbstractKrid<E>.isEmptyElement: (E) -> Boolean get() = { it == emptyElement }

/**
 * Convenience extension for [AbstractKrid.get].
 */
operator fun <E> AbstractKrid<E>.get(cell: Cell) = get(cell.x, cell.y)

operator fun <E> AbstractKrid<E>.get(cells: List<Cell>): List<CellValue<E>> {
  dimension.filterNotInBounds(cells).also {
    require(it.isEmpty()) { "Cannot get() because some cells are out of bounds: $it." }
  }

  return cells.map { cell(it, get(it)) }
}

fun <E> AbstractKrid<E>.ascii(toChar: (E) -> Char? = { it?.toString()?.first() ?: '.' }): String = this.rows().joinToString(separator = "\n") {
  it.map { e -> toChar(e) ?: '.' }.joinToString(separator = "")
}

