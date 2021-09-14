package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Dimension

/**
 * Core Utility class. Provides factory methods to create krid instances and
 * helper methods and extensions for convenient usage.
 */
object Krids {

  fun <E> krid(emptyElement: E) = krid(1, 1, emptyElement)

  fun <E> krid(width: Int, height: Int, emptyElement: E) = krid(width, height, emptyElement) { _, _ -> emptyElement }

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

  fun indexToCell(width: Int): (Int) -> Cell = { Cell(it / width, it % width) }
  fun cellToIndex(width: Int): (Cell) -> Int = { it.x * width + it.y }

  fun Pair<Int, Int>.toCell() = cell(first, second)

}
