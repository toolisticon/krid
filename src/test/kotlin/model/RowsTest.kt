package io.toolisticon.lib.krid.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class RowsTest {

  @Test
  fun `create rows`() {
    val row = Row(0, listOf(true))
    val rows: Rows<Boolean> = Rows(row)

    assertThat(rows[0]).isEqualTo(row)
  }
}
