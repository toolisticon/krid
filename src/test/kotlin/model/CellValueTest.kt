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
}
