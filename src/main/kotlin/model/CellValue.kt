package io.toolisticon.lib.krid.model

data class CellValue<E>(val x: Int, val y: Int, override val value: E) : Map.Entry<Cell, E> {
  constructor(cell: Cell, value: E) : this(cell.x, cell.y, value)
  constructor(pair: Pair<Int, Int>, value: E) : this(pair.toCell(), value = value)

  val cell = Cell(x, y)
  override val key = cell

  operator fun plus(cell: Cell): CellValue<E> = (this.cell + cell).let {
    copy(x = it.x, y = it.y)
  }
}

val <E> List<CellValue<E>>.cells get() = map { it.cell }
