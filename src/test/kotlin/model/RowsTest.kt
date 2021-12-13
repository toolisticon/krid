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

  @Test
  fun `to columns`() {
    val rows: Rows<Int> = Rows(
      Row(0, listOf(0, 1, 2)),
      Row(1, listOf(3, 4, 5)),
    )

    val cols = rows.toColumns()

    assertThat(cols).hasSize(3)
    assertThat(cols[0]).isEqualTo(Column(0, listOf(0, 3)))
    assertThat(cols[1]).isEqualTo(Column(1, listOf(1, 4)))
    assertThat(cols[2]).isEqualTo(Column(2, listOf(2, 5)))
  }
}
