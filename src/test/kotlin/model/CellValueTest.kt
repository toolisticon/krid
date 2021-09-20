package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class CellValueTest {

  @Test
  internal fun `add cell`() {
    val cell = cell(2, 3, true)

    assertThat(cell + cell(5, 6)).isEqualTo(cell(7, 9, true))
  }

  @Test
  internal fun `create cell value boolean`() {
    val cellValue: CellValue<Boolean> = CellValue(1, 2, true)

    assertThat(CellValue(Cell(1, 2), true)).isEqualTo(cellValue)
    assertThat(CellValue(Cell(1, 2), true).cell).isEqualTo(Cell(1, 2))
  }

}
