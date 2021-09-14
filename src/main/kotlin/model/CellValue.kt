package io.toolisticon.lib.krid.model

data class CellValue<E>(val x: Int, val y: Int, val value: E) {
  companion object {
    val CellValue<*>.cell: Cell get() = Cell(x = x, y = y)
  }

  operator fun plus(cell: Cell) : CellValue<E> = (this.cell + cell).let {
    copy(x = it.x, y=it.y)
  }
}


