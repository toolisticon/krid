package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.cell

/**
 * Command that wraps the parameters needed to add a krid to another krid.
 */
data class AddKrid<E>(
  /**
   * At which offset do you want to insert the upper left corner of krid?
   */
  val offset: Cell = cell(0, 0),

  /**
   * The krid you want to add.
   */
  val krid: Krid<E>,

  /**
   * A transformation that takes the old value and the new value and calculates the new value for the cell.
   * By default, this will just take the new value.
   */
  val operation: (E, E) -> E = { _, new -> new }
) {

  /**
   * Gets a value from the internal krid, using cell coordinates from the target krid.
   * This means, that the offset is substracted from the target cell before getting the value.
   */
  operator fun get(targetCell: Cell): E = with(targetCell - offset) { krid[x, y] }

  /**
   * Calculates
   */
  operator fun invoke(old: CellValue<E>): CellValue<E> = cell(old.x, old.y, operation(old.value, get(old.cell)))

  val cells by lazy {
    krid.sequence().map { it.cell + offset }.toList()
  }

  val cellValues by lazy {
    krid.sequence().map { it + offset }.toList()
  }

}
