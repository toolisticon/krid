package io.toolisticon.lib.krid.model

/**
 * Represents a row (horizontal line) in a [io.toolisticon.lib.krid.Krid].
 */
data class Row<E>(val index: Int, val values: List<E>) : List<E> by values

/**
 * Represents a collection of rows (horizontal lines) in a [io.toolisticon.lib.krid.Krid].
 */
data class Rows<E>(val rows: List<Row<E>>) : List<Row<E>> by rows {
  constructor(vararg rows: Row<E>) : this(rows = rows.toList())
}
