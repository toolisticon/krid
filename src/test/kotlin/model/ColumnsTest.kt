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

  @Test
  fun `to rows`() {
    val cols = Columns(
      Column(0, listOf(0, 1, 2)),
      Column(1, listOf(3, 4, 5)),
    )

    val rows = cols.toRows()
    assertThat(rows).hasSize(3)

    assertThat(rows[0]).isEqualTo(Row(0, listOf(0, 3)))
    assertThat(rows[1]).isEqualTo(Row(1, listOf(1, 4)))
    assertThat(rows[2]).isEqualTo(Row(2, listOf(2, 5)))
  }
}
