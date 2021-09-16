package io.toolisticon.lib.krid.model

/**
 * Represents width and height of a [io.toolisticon.lib.krid.Krid].
 */
data class Dimension(
  val width: Int,
  val height: Int
) {
  companion object {
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
   * Filters given cells and returns only those that are in the bounds of this dimension.
   */
  fun filterInBound(cells:List<Cell>) : List<Cell> = cells.filter { it.x in columnRange && it.y in rowRange }

}

/**
 * Vararg extension for [Dimension.filterInBound].
 */
fun Dimension.filterInBound(vararg cells:Cell) : List<Cell> = filterInBound(cells.toList())
