package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.*

abstract class AbstractKrid<E> {
  abstract val dimension: Dimension
  abstract val emptyElement: E

  abstract fun isEmpty() : Boolean
  abstract operator fun get(x:Int, y:Int) : E

  fun rows() : Rows<E> =  Rows(dimension.rowRange.map(this::row))
  fun columns() : Columns<E> = Columns(dimension.columnRange.map(this::column))

  abstract fun row(index: Int) : Row<E>
  abstract fun column(index: Int) : Column<E>

  abstract fun iterator() : Iterator<CellValue<E>>
}

internal val <E : Any?>AbstractKrid<E>.isEmptyElement: (E) -> Boolean get() = { it == emptyElement }
operator fun <E : Any?>AbstractKrid<E>.get(cell: Cell) = get(cell.x, cell.y)

fun <E> AbstractKrid<E>.ascii(toChar: (E) -> Char? = { it?.toString()?.first() ?: '.' }): String = this.rows().joinToString(separator = "\n") {
  it.map { e -> toChar(e) ?: '.' }.joinToString(separator = "")
}
