package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.CellTest.Comparison.*
import io.toolisticon.lib.krid.model.step.Direction
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class CellTest {
  enum class Comparison { LT, EQ, GT }

  @ParameterizedTest
  @CsvSource(
    value = [
      "0 to 0, 0 to 0, EQ",
      "0 to 0, 0 to 1, LT",
      "0 to 0, 1 to 0, LT",
      "0 to 0, 1 to 1, LT",

      "0 to 1, 0 to 0, GT",
      "0 to 1, 0 to 1, EQ",
      "0 to 1, 1 to 0, LT",
      "0 to 1, 1 to 1, LT",

      "1 to 0, 0 to 0, GT",
      "1 to 0, 0 to 1, LT",
      "1 to 0, 1 to 0, EQ",
      "1 to 0, 1 to 1, LT",

      "1 to 1, 0 to 0, GT",
      "1 to 1, 0 to 1, GT",
      "1 to 1, 1 to 0, GT",
      "1 to 1, 1 to 1, EQ",
    ]
  )
  fun `compare cells`(@ConvertWith(CellConverter::class) c1: Cell, @ConvertWith(CellConverter::class) c2: Cell, expected: Comparison) {
    when (expected) {
      LT -> assertThat(c1).isLessThan(c2)
      EQ -> assertThat(c1).isEqualByComparingTo(c2)
      GT -> assertThat(c1).isGreaterThan(c2)
    }
  }

  @Test
  fun `create a cell from pair`() {
    assertThat((2 to 2).toCell()).isEqualTo(cell(2, 2))
  }

  @Test
  fun `sum two cells - add x and y coordinates`() {
    assertThat(cell(2, 3) + cell(5, 7))
      .isEqualTo(cell(7, 10))
  }

  @Test
  fun `difference of two cells - subtract x and y coordinates`() {
    assertThat(cell(2, 3) - cell(1, 2))
      .isEqualTo(cell(1, 1))
  }

  @Test
  fun `cell minus cell - fails when other is not lower to the right`() {
    assertThatThrownBy { cell(0, 0) - cell(1, 0) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("Cell(x=0, y=0) has to be >= Cell(x=1, y=0).")
  }


  @Test
  fun `orthogonal adjacent cells`() {
    val seed = cell(1, 1)
    assertThat(seed.orthogonalAdjacent)
      .isEqualTo(
        listOf(
          1 to 0,
          2 to 1,
          1 to 2,
          0 to 1,
        ).toCells()
      )
  }

  @Test
  fun `adjacent cells`() {
    val seed = cell(1, 1)
    assertThat(seed.adjacent)
      .containsExactlyInAnyOrder(
        Direction.UP(seed),
        Direction.UP_RIGHT(seed),
        Direction.RIGHT(seed),
        Direction.DOWN_RIGHT(seed),
        Direction.DOWN(seed),
        Direction.DOWN_LEFT(seed),
        Direction.LEFT(seed),
        Direction.UP_LEFT(seed),
      )
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "UP, 1 to 0",
      "UP_RIGHT, 2 to 0",
      "RIGHT, 2 to 1",
      "DOWN_RIGHT, 2 to 2",
      "DOWN, 1 to 2",
      "DOWN_LEFT, 0 to 2",
      "LEFT, 0 to 1",
      "UP_LEFT, 0 to 0",
    ]
  )
  fun directions(direction: Direction, @ConvertWith(CellConverter::class) expected: Cell) {
    val cell = cell(1, 1)

    assertThat(cell(direction)).isEqualTo(expected)
  }
}
