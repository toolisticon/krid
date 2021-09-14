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

  val size = width * height
  val rowRange: IntRange = 0 until height
  val columnRange: IntRange = 0 until width
  val pair = width to height
}

