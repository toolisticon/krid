package io.toolisticon.lib.krid.model

data class Column<E>(val index: Int, val values: List<E>) : List<E> by values

data class Columns<E>(val columns: List<Column<E>>) : List<Column<E>> by columns
