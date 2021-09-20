package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.*
import io.toolisticon.lib.krid.model.Cell.Companion.requireGreaterThanOrEqual

/**
 * A [Krid] of type `<E>` with given [Dimension].
 */
data class Krid<E>(
  override val dimension: Dimension,
  override val emptyElement: E,
  override val list: List<E>
) : AbstractKrid<E>() {

  init {
    require(dimension.size == list.size)
  }

  private val rowCache: MutableMap<Int, Row<E>> = mutableMapOf()
  private val columnCache: MutableMap<Int, Column<E>> = mutableMapOf()

  override fun row(index: Int): Row<E> = rowCache.computeIfAbsent(
    requireInRows(index)
  ) { Row(index, dimension.columnRange.map { get(it, index) }) }

  override fun column(index: Int): Column<E> = columnCache.computeIfAbsent(
    requireInColumns(index)
  ) { Column(index, dimension.rowRange.map { get(index, it) }) }

  operator fun plus(cellValues: List<CellValue<E>>): Krid<E> {
    dimension.filterNotInBounds(cellValues.cells).also {
      require(it.isEmpty()) { "Cannot modify values because cells are out of bounds: $it." }
    }

    val mutable = list.toMutableList()

    cellValues.forEach {
      mutable[indexTransformer.toIndex(it.cell)] = it.value
    }
    return copy(
      list = mutable.toList()
    )
  }

  fun subKrid(upperLeft: Cell, lowerRight: Cell): Krid<E> {
    require(dimension.isInBounds(upperLeft)) { "$upperLeft is out of bounds (dimension=$dimension)." }
    require(dimension.isInBounds(lowerRight)) { "$lowerRight is out of bounds (dimension=$dimension)." }

    val newDimension = Dimension(upperLeft, lowerRight)

    return krid(
      dimension = newDimension,
      emptyElement = emptyElement,
      initialize = { x, y ->
        get(x + upperLeft.x, y + upperLeft.y)
      }
    )
  }
}

operator fun <E> Krid<E>.plus(cellValue: CellValue<E>): Krid<E> = plus(listOf(cellValue))
