package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Dimension

abstract class AbstractKrid<E> {
  abstract val dimension: Dimension
  abstract val emptyElement: E

  abstract fun isEmpty() : Boolean
  abstract operator fun get(x:Int, y:Int) : E
}

internal val <E : Any?>AbstractKrid<E>.isEmptyElement: (E) -> Boolean get() = { it == emptyElement }
operator fun <E : Any?>AbstractKrid<E>.get(cell: Cell) = get(cell.x, cell.y)
