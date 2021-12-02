package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Coordinates

/**
 * The "mother of all stepFn's" ... stores the total (x,y) offset
 * and applies it to a cell.
 */
data class CoordinatesStep(private val coordinates: Cell = Krids.ORIGIN) : StepFn, Coordinates by coordinates {
  constructor(x: Int, y: Int) : this(Krids.cell(x, y))

  override fun plus(other: StepFn): CoordinatesStep = copy(coordinates = other(coordinates))

  override fun times(number: Int): StepFn = copy(coordinates = coordinates * number)

  override fun invoke(start: Cell): Cell = start + coordinates

  val directionSteps: CompositeStep by lazy {
    CompositeStep(coordinates)
  }

  override fun toString(): String = "CoordinatesStep(x=$x, y=$y)"
}
