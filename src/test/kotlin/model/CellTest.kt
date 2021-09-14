package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.CellTest.Companion.Comparison.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class CellTest {
  companion object {
    enum class Comparison { LT, EQ, GT }

    /**
     * 00 01
     * 10 11
     */
    @JvmStatic
    fun `compare cells - parameters`(): Stream<Arguments> = Stream.of(
      // 00 EQ 00
      arguments(cell(0, 0), cell(0, 0), EQ),
      // 00 LT 01
      arguments(cell(0, 0), cell(0, 1), LT),
      // 00 LT 10
      arguments(cell(0, 0), cell(1, 0), LT),
      // 00 LT 11
      arguments(cell(0, 0), cell(1, 1), LT),

      // 01 GT 00
      arguments(cell(0, 1), cell(0, 0), GT),
      // 01 EQ 01
      arguments(cell(0, 1), cell(0, 1), EQ),
      // 01 LT 10
      arguments(cell(0, 1), cell(1, 0), LT),
      // 01 LT 11
      arguments(cell(0, 1), cell(1, 1), LT),

      // 10 GT 00
      arguments(cell(1, 0), cell(0, 0), GT),
      // 10 LT 01
      arguments(cell(1, 0), cell(0, 1), LT),
      // 10 LT 10
      arguments(cell(1, 0), cell(1, 0), EQ),
      // 10 LT 11
      arguments(cell(1, 0), cell(1, 1), LT),

      // 11 GT 00
      arguments(cell(1, 1), cell(0, 0), GT),
      // 11 GT 01
      arguments(cell(1, 1), cell(0, 1), GT),
      // 11 GT 10
      arguments(cell(1, 1), cell(1, 0), GT),
      // 11 GT 11
      arguments(cell(1, 1), cell(1, 1), EQ),
    )


  }

  @ParameterizedTest
  @MethodSource("compare cells - parameters")
  internal fun `compare cells`(c1: Cell, c2: Cell, expected: Comparison) {
    when (expected) {
      LT -> assertThat(c1).isLessThan(c2)
      EQ -> assertThat(c1).isEqualByComparingTo(c2)
      GT -> assertThat(c1).isGreaterThan(c2)
    }
  }



  @Test
  internal fun `create a cell from pair`() {
    val cell = cell(2, 2)

    println(cell)
  }

  @Test
  internal fun `sum two cells - add x and y coordinates`() {
    assertThat(cell(2, 3) + cell(5, 7))
      .isEqualTo(cell(7, 10))
  }

  @Test
  internal fun `difference of two cells - subtract x and y coordinates`() {
    assertThat(cell(2, 3) - cell(1, 2))
      .isEqualTo(cell(1, 1))
  }

}
