package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class DimensionTest {

  @Test
  internal fun `create with width and height`() {
    val dimension = Dimension(5, 4)

    assertThat(dimension.width).isEqualTo(5)
    assertThat(dimension.height).isEqualTo(4)
    assertThat(dimension.size).isEqualTo(20)
    assertThat(dimension.rowRange).isEqualTo(0..3)
    assertThat(dimension.columnRange).isEqualTo(0..4)
  }

  @Test
  internal fun `create from two cells - success`() {
    val dimension = Dimension(cell(1, 0), cell(2, 3))
    assertThat(dimension.width).isEqualTo(2)
    assertThat(dimension.height).isEqualTo(4)
    assertThat(dimension.size).isEqualTo(8)
  }

  @Test
  internal fun `create from two cells - failure`() {
    assertThatThrownBy { Dimension(cell(2, 3), cell(1, 0)) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("Cell(x=1, y=0) has to be right and/or down from Cell(x=2, y=3).")
  }

  @Test
  internal fun `fails when values are not gt 0`() {
    assertThatThrownBy { Dimension(width = 0, height = 1) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("Values have to be positive numbers (was: Dimension(width=0, height=1)).")

    assertThatThrownBy { Dimension(width = 1, height = 0) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("Values have to be positive numbers (was: Dimension(width=1, height=0)).")

    assertThatThrownBy { Dimension(width = 0, height = 0) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("Values have to be positive numbers (was: Dimension(width=0, height=0)).")
  }

  @Test
  internal fun `cells in bound`() {
    val dimension = Dimension(3, 2)

    assertThat(
      dimension.filterInBounds(
        cell(5, 5)
      )
    ).isEmpty()

    assertThat(
      dimension.filterInBounds(
        cell(0, 0),
        cell(1, 1),
        cell(3, 0),
        cell(0, 2),
      )
    ).containsExactly(
      cell(0, 0),
      cell(1, 1),
    )
  }

  @Test
  internal fun `cells not in bound`() {
    val dimension = Dimension(3, 2)

    assertThat(
      dimension.filterNotInBounds(
        cell(0, 0),
        cell(1, 1),
        cell(3, 0),
        cell(0, 2),
      )
    ).containsExactly(
      cell(3, 0),
      cell(0, 2),
    )
  }

  @Test
  internal fun `predicate is in bounds`() {
    val dimension = Dimension(2, 2)

    assertThat(dimension.isInBounds(cell(1, 1))).isTrue
    assertThat(dimension.isInBounds(cell(1, 2))).isFalse
  }
}
