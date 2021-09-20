package io.toolisticon.lib.krid.model

/**
 * Represents width and height of a [io.toolisticon.lib.krid.Krid].
 */
data class Dimension(
  val width: Int,
  val height: Int
) {
  companion object {
    /**
     * Creates [Dimension] based on x/y diff of given cells.
     *
     * @throws IllegalArgumentException when [upperLeft] is not < [lowerRight].
     */
    operator fun invoke(upperLeft: Cell, lowerRight: Cell): Dimension {
      require(lowerRight >= upperLeft) { "$upperLeft has to be right and/or down from $lowerRight." }
      return Dimension(
        width = lowerRight.x - upperLeft.x + 1,
        height = lowerRight.y - upperLeft.y + 1
      )
    }
  }

  init {
    require(width > 0 && height > 0) { "Values have to be positive numbers (was: $this)." }
  }

  /**
   * The total area of the 2D grid (width*height), which is also the size of an implementing List.
   */
  val size = width * height

  /**
   * the row indices counting from `0` to `height - 1`
   */
  val rowRange: IntRange = 0 until height

  /**
   * the column indices counting from `0` to `width - 1`
   */
  val columnRange: IntRange = 0 until width

  /**
   * Predicate that checks if the given cell is in the row- and col-ranges of this dimension.
   */
  fun isInBounds(cell: Cell): Boolean = cell.x in columnRange && cell.y in rowRange

  /**
   * Filters given cells and returns only those that are in the bounds of this dimension.
   */
  fun filterInBounds(cells: List<Cell>): List<Cell> = cells.filter(this::isInBounds)

  /**
   * Filters given cells and returns only those that are not in the bounds of this dimension.
   */
  fun filterNotInBounds(cells: List<Cell>): List<Cell> = cells.filterNot(this::isInBounds)
}

/**
 * Vararg extension for [Dimension.filterInBounds].
 */
fun Dimension.filterInBounds(vararg cells: Cell): List<Cell> = filterInBounds(cells.toList())

/**
 * Vararg extension for [Dimension.filterNotInBounds].
 */
fun Dimension.filterNotInBounds(vararg cells: Cell): List<Cell> = filterNotInBounds(cells.toList())

/**
 * [Dimension.width] and [Dimension.height] as [Pair].
 */
val Dimension.pair: Pair<Int, Int> get() = width to height
