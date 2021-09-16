package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Dimension

/**
 * Core Utility class. Provides factory methods to create krid instances and
 * helper methods and extensions for convenient usage.
 */
object Krids {

  /**
   * Creates a new [Krid] with [Dimension](1,1) containing just the emptyElement value.
   */
  fun <E> krid(emptyElement: E) = krid(1, 1, emptyElement)

  /**
   * Creates a new [Krid] with [Dimension](width, height), all cells initialized with the emptyElement value.
   */
  fun <E> krid(width: Int, height: Int, emptyElement: E) = krid(width, height, emptyElement) { _, _ -> emptyElement }

  /**
   * Creates a new [Krid] with [Dimension](width, height), all cells initialized by the given BiFunction.
   */
  fun <E> krid(width: Int, height: Int, emptyElement: E, initialize: (Int, Int) -> E): Krid<E> {
    val cellOf = indexToCell(width)
    return Krid(
      dimension = Dimension(width, height),
      emptyElement = emptyElement,
      list = List(size = width * height) { index ->
        cellOf(index).let { initialize(it.x, it.y) }
      }
    )
  }

  /**
   * Creates Krid<Char> from given String.
   */
  fun krid(string: String, emptyElement: Char = '.'): Krid<Char> = krid(string, emptyElement) { it }


  fun <E> krid(string: String, emptyElement: E, parse: (Char) -> E): Krid<E> = krid(
    rows = string.lines().map { it.trim() }
      .filterNot { it.isEmpty() }
      .map { it.map(parse) },
    emptyElement = emptyElement
  )

  fun <E> krid(rows: List<List<E>>, emptyElement: E): Krid<E> {
    require(rows.isNotEmpty()) { "rows must not be empty: $rows" }
    require(rows.none { it.isEmpty() }) { "no rows must be empty: $rows" }
    require(rows.maxOf { it.size } == rows.minOf { it.size }) { "all rows must have same size: $rows" }

    return Krid(
      dimension = Dimension(width = rows[0].size, height = rows.size),
      emptyElement = emptyElement,
      list = rows.fold<List<E>, List<E>>(listOf()) { list, row -> list + row }.toList()
    )
  }

  fun cell(x: Int, y: Int) = Cell(x, y)
  fun <E> cell(x: Int, y: Int, value: E) = CellValue(x, y, value)
  fun <E> cell(cell: Cell, value: E) = cell(cell.x, cell.y, value)

  /**
   * Given the width of the Krid, this calculates the Cell(x,y) coordinates based on the list index.
   */
  fun indexToCell(width: Int): (Int) -> Cell = { Cell(it % width, it / width) }

  /**
   * Given the width of the Krid, this calculates the the list index value  based on the Cell(x,y) coordinates.
   */
  fun cellToIndex(width: Int): (Cell) -> Int = { it.y * width + it.x }

  /**
   * Transforms a pair of ints to a type safe [Cell].
   */
  fun Pair<Int, Int>.toCell() = cell(first, second)

  fun List<Pair<Int,Int>>.toCells() = map { it.toCell() }

  val Dimension.pair: Pair<Int, Int> get() = width to height
//
//  fun <E> Krid<E>.iterator(): Iterator<CellValue<E>> = sequence {
//    val cellOf = indexToCell(dimension.width)
//    list.forEachIndexed { i, e -> yield(CellValue(cellOf(i), e)) }
//  }.iterator()
}
