package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cellToIndex
import io.toolisticon.lib.krid.Krids.indexToCell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.Krids.toCell
import io.toolisticon.lib.krid.model.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class KridsTest {
  companion object {
    /**
     * width = 3, height = 2
     * 0,t = 0,0,t
     * 1,f = 0,1,f
     * 2,t = 0,2,t
     *
     * 3,f = 1,0,f
     * 4,t = 1,1,t
     * 5,f = 1,2,t
     */
    @JvmStatic
    fun `index to cell and back - parameters`(): Stream<Arguments> = Stream.of(
      arguments(3, 0, 0 to 0),
      arguments(3, 1, 0 to 1),
      arguments(3, 2, 0 to 2),
      arguments(3, 3, 1 to 0),
      arguments(3, 4, 1 to 1),
      arguments(3, 5, 1 to 2),
    )
  }

  @ParameterizedTest
  @MethodSource("index to cell and back - parameters")
  internal fun `index to cell and back`(width: Int, index: Int, pair: Pair<Int, Int>) {
    val cell = pair.toCell()
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
    val krid = krid("""
      ....
      .abc
      .def
    """.trimIndent())

    assertThat(krid[2,1]).isEqualTo('b')
  }

  @Test
  internal fun `init single empty`() {
    val krid: Krid<Boolean?> = Krids.krid(null)

    assertThat(krid.dimension.pair).isEqualTo(1 to 1)
    assertThat(krid.emptyElement).isEqualTo(null)
    assertThat(krid.isEmpty()).isTrue
  }

}
