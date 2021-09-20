package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid._test.BooleanKridHelper.booleanCellValue
import io.toolisticon.lib.krid._test.BooleanKridHelper.booleanKrid
import io.toolisticon.lib.krid._test.isInstanceOf
import io.toolisticon.lib.krid.model.Dimension
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class KridTest {

  @Test
  internal fun `init fails - dimension#size != list#size`() {
    assertThatThrownBy { Krid(Dimension(4, 5), true, List<Boolean>(40) { false }) }
      .isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  internal fun `create empty with dimension`() {
    val krid: Krid<Boolean?> = krid(4, 3, null)
    assertThat(krid.isEmpty()).isTrue
    assertThat(krid.dimension.width).isEqualTo(4)
    assertThat(krid.dimension.height).isEqualTo(3)
  }

  @Test
  internal fun `get rows`() {
    val krid = booleanKrid(
      """
      tff
      ftf
      fft
    """.trimIndent()
    )

    assertThat(krid.row(0)).containsExactly(
      true, false, false
    )
    assertThat(krid.row(2)).containsExactly(
      false, false, true
    )
  }

  @Test
  internal fun `get columns`() {
    val krid = booleanKrid(
      """
      tff
      ftt
      fft
    """.trimIndent()
    )

    assertThat(krid.column(0)).containsExactly(
      true, false, false
    )
    assertThat(krid.column(2)).containsExactly(
      false, true, true
    )
  }

  @Test
  internal fun `iterate value cells`() {
    val krid = booleanKrid(
      """
      t.
      ft
    """.trimIndent()
    )

    assertThat(krid.iterator().asSequence().toList())
      .containsExactly(
        booleanCellValue(0, 0, true),
        booleanCellValue(1, 0, null),
        booleanCellValue(0, 1, false),
        booleanCellValue(1, 1, true),
      )
  }

  @Test
  internal fun `set cell value`() {
    var krid = booleanKrid(
      """
      t.
      ft
    """.trimIndent()
    )

    krid += cell(1, 0, true)

    assertThat(krid.row(0)).containsExactly(true, true)
    assertThat(krid.row(1)).containsExactly(false, true)

    krid += listOf(cell(1, 0, false), cell(0, 1, true))
    assertThat(krid.row(0)).containsExactly(true, false)
    assertThat(krid.row(1)).containsExactly(true, true)
  }

  @Test
  internal fun `fail to set cell values if cell out of bounds`() {
    var krid = booleanKrid(
      """
      t.
      ft
    """.trimIndent()
    )

    assertThatThrownBy {
      krid += listOf(
        cell(0, 0, false),
        cell(2, 0, true),
        cell(2, 2, null)
      )
    }.isInstanceOf(IllegalArgumentException::class)
      .hasMessage("Cannot modify values because cells are out of bounds: [Cell(x=2, y=0), Cell(x=2, y=2)].")


  }
}
