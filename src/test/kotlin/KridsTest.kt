package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cellToIndex
import io.toolisticon.lib.krid.Krids.indexToCell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.Krids.pair
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class KridsTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "3, 0, 0 to 0",
      "3, 1, 1 to 0",
      "3, 2, 2 to 0",
      "3, 3, 0 to 1",
      "3, 4, 1 to 1",
      "3, 5, 2 to 1",
    ]
  )
  internal fun `index to cell and back`(width: Int, index: Int, @ConvertWith(CellConverter::class) cell: Cell) {
    val toCell = indexToCell(width)
    val toIndex = cellToIndex(width)

    assertThat(toCell(index)).isEqualTo(cell)
    assertThat(toIndex(cell)).isEqualTo(index)
  }

  @Test
  internal fun `dummy test`() {
    assertThat(1 + 1).isEqualTo(2)
  }

  @Test
  internal fun `create krid(char)`() {
    val krid = krid(
      """
      ....
      .abc
      .def
    """.trimIndent()
    )

    assertThat(krid[2, 1]).isEqualTo('b')
  }

  @Test
  internal fun `init single empty`() {
    val krid: Krid<Boolean?> = Krids.krid(null)

    assertThat(krid.dimension.pair).isEqualTo(1 to 1)
    assertThat(krid.emptyElement).isEqualTo(null)
    assertThat(krid.isEmpty()).isTrue
    assertThat(krid.row(0)).isEqualTo(Row(0, listOf(null)))
    assertThat(krid.column(0)).isEqualTo(Column(0, listOf(null)))
  }

  @Test
  internal fun `create new krid from init function`() {
    val krid: Krid<Boolean?> = krid(3, 4, null) { x, y -> x == y }

    assertThat(krid[0, 0]).isTrue
    assertThat(krid[1, 1]).isTrue
    assertThat(krid[2, 3]).isFalse

    assertThat(krid.column(0)).containsExactly(true, false, false, false)
    assertThat(krid.column(1)).containsExactly(false, true, false, false)
    assertThat(krid.column(2)).containsExactly(false, false, true, false)

  }
}
