package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.Dimension

data class Krid<E>(
  override val dimension: Dimension,
  override val emptyElement: E,
  private val list: List<E>
) : AbstractKrid<E>() {

  init {
    require(dimension.size == list.size)
  }

  override fun get(x: Int, y: Int): E = list[requireInRows(y) * dimension.width + requireInColumns(x)]

  override fun isEmpty(): Boolean = list.all { isEmptyElement(it) }

  private fun requireInRows(index: Int): Int = index.apply { require(index in dimension.rowRange) { "$this has to be in ${dimension.rowRange}." } }
  private fun requireInColumns(index: Int): Int = index.apply { require(this in dimension.columnRange) { "$this has to be in ${dimension.columnRange}." } }
}
