package io.toolisticon.lib.krid.model

data class Row<E>(val index: Int, val values: List<E>) : List<E> by values

data class Rows<E>(val rows: List<Row<E>>) : List<Row<E>> by rows {
  constructor(vararg rows: Row<E>) : this(rows = rows.toList())
}
