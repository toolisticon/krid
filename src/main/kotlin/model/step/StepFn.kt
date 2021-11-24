package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Coordinates

/**
 * Transformation of a cell to another cell.
 */
sealed interface StepFn : (Cell) -> Cell {
  companion object {
    fun step(x: Int, y: Int) = CoordinatesStep(x = x, y = y)
    fun step(coordinates: Coordinates) = step(x = coordinates.x, y = coordinates.y)
  }

  /**
   * Combine this function with another function.
   *
   * @param other fn to add to existing
   * @return combined function
   */
  operator fun plus(other: StepFn): StepFn

  /**
   * Applies step operation to given cell.
   *
   * @param start start position (x,y coordinates)
   * @return target position (x,y coordinates)
   */
  override fun invoke(start: Cell): Cell
}
