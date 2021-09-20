package io.toolisticon.lib.krid.fn

import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource


internal class IndexTransformerTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "3, 0, 0 to 0",
      "3, 1, 1 to 0",
      "3, 2, 2 to 0",
      "3, 3, 0 to 1",
      "3, 4, 1 to 1",
      "3, 5, 2 to 1",
      "4, 3, 3 to 0",
      "4, 4, 0 to 1",
    ]
  )
  internal fun `index to cell and back`(width: Int, index: Int, @ConvertWith(CellConverter::class) cell: Cell) {
    val transformer = IndexTransformer(width)

    assertThat(transformer.toCell(index)).isEqualTo(cell)
    assertThat(transformer.toIndex(cell)).isEqualTo(index)
    assertThat(transformer.toIndex(cell.x, cell.y)).isEqualTo(index)
  }
}
