package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.cell

data class AddKrid<E>(
  val offset: Cell = cell(0, 0),
  val krid: Krid<E>,
  val operation: (E, E) -> E = { _, new -> new }
) {

  val cells by lazy {
    krid.iterator().asSequence().map { it.cell }.toList()
  }

  fun values(): List<CellValue<E>> = krid.iterator()
    .asSequence()
    .map { it + offset }
    .toList()

}
