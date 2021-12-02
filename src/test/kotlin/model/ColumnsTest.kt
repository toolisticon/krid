package io.toolisticon.lib.krid.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ColumnsTest {

  @Test
  fun `create columns`() {
    val column = Column(0, listOf(true))

    val columns: Columns<Boolean> = Columns(column)

    assertThat(columns[0]).isEqualTo(column)
  }
}
