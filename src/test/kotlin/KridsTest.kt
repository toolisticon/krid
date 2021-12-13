package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Row
import io.toolisticon.lib.krid.model.pair
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class KridsTest {

  @Test
  fun `dummy test`() {
    assertThat(1 + 1).isEqualTo(2)
  }

  @Test
  fun `create krid(char)`() {
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
  fun `init single empty`() {
    val krid: Krid<Boolean?> = krid(null)

    assertThat(krid.dimension.pair).isEqualTo(1 to 1)
    assertThat(krid.emptyElement).isEqualTo(null)
    assertThat(krid.isEmpty()).isTrue
    assertThat(krid.row(0)).isEqualTo(Row(0, listOf(null)))
    assertThat(krid.column(0)).isEqualTo(Column(0, listOf(null)))
  }

  @Test
  fun `create new krid from init function`() {
    val krid: Krid<Boolean?> = krid(3, 4, null) { x, y -> x == y }

    assertThat(krid[0, 0]).isTrue
    assertThat(krid[1, 1]).isTrue
    assertThat(krid[2, 3]).isFalse

    assertThat(krid.column(0)).containsExactly(true, false, false, false)
    assertThat(krid.column(1)).containsExactly(false, true, false, false)
    assertThat(krid.column(2)).containsExactly(false, false, true, false)
  }

  @Test
  fun `create from rows`() {
    val krid: Krid<Boolean?> = krid(
      listOf(
        listOf(true, null),
        listOf(null, false),
      ), null
    )

    assertThat(krid.column(0)).containsExactly(true, null)
    assertThat(krid.column(1)).containsExactly(null, false)
  }


  @Test
  fun `fromRows fails when rows is empty`() {
    assertThatThrownBy { krid(emptyList(), false) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("rows must not be empty: []")
  }

  @Test
  fun `fromRows fails when a row is empty`() {
    assertThatThrownBy { krid(listOf(listOf(true), emptyList()), false) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("no rows must be empty: [[true], []]")
  }

  @Test
  fun `fromRows fails when rows have different size`() {
    assertThatThrownBy { krid(listOf(listOf(true, true), listOf(true)), false) }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessage("all rows must have same size: [[true, true], [true]]")
  }
}
