package io.toolisticon.lib.krid.model

/**
 * Represents a column (vertical line) in a [io.toolisticon.lib.krid.Krid].
 */
data class Column<E>(val index: Int, val values: List<E>) : List<E> by values

/**
 * Represents a collection of columns (vertical lines) in a [io.toolisticon.lib.krid.Krid].
 */
data class Columns<E>(val columns: List<Column<E>>) : List<Column<E>> by columns {
  constructor(vararg columns: Column<E>) : this(columns.toList())
}

fun <E> Columns<E>.toRows(): Rows<E> {
  val indices = this.columns.first().indices

  return Rows(indices.map {
    Row(it, columns.map { c -> c[it] })
  })
}
