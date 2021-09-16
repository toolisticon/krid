package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.indexToCell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Dimension
import io.toolisticon.lib.krid.model.Row

data class Krid<E>(
  override val dimension: Dimension,
  override val emptyElement: E,
  private val list: List<E>
) : AbstractKrid<E>() {

  init {
    require(dimension.size == list.size)
  }

  private val indexToCell = indexToCell(dimension.width)
  private val rowCache: MutableMap<Int, Row<E>> = mutableMapOf()
  private val columnCache: MutableMap<Int, Column<E>> = mutableMapOf()
  private fun requireInRows(index: Int): Int = index.apply { require(index in dimension.rowRange) { "$this has to be in ${dimension.rowRange}." } }
  private fun requireInColumns(index: Int): Int = index.apply { require(this in dimension.columnRange) { "$this has to be in ${dimension.columnRange}." } }

  override fun get(x: Int, y: Int): E = list[requireInRows(y) * dimension.width + requireInColumns(x)]

  override fun isEmpty(): Boolean = list.all { isEmptyElement(it) }

  override fun row(index: Int): Row<E> = rowCache.computeIfAbsent(
    requireInRows(index)
  ) { Row(index, dimension.columnRange.map { get(it, index) }) }

  override fun column(index: Int): Column<E> = columnCache.computeIfAbsent(
    requireInColumns(index)
  ) { Column(index, dimension.rowRange.map { get(index, it) }) }

  override fun iterator(): Iterator<CellValue<E>> = list
    .mapIndexed { index, e -> cell(indexToCell(index), e) }
    .iterator()

}

