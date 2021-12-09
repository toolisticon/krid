package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid._test.BooleanKridHelper.booleanCellValue
import io.toolisticon.lib.krid._test.BooleanKridHelper.booleanKrid
import io.toolisticon.lib.krid._test.ResourceHelper
import io.toolisticon.lib.krid._test.isInstanceOf
import io.toolisticon.lib.krid.model.Dimension
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.StepFn.Companion.step
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class KridTest {

  private val innerKrid = booleanKrid(
    """
      ....
      .tf.
      .ft.
      ....
    """.trimIndent()
  )

  @Test
  fun `init fails - dimension#size != list#size`() {
    assertThatThrownBy { Krid(Dimension(4, 5), true, List<Boolean>(40) { false }) }
      .isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun `create empty with dimension`() {
    val krid: Krid<Boolean?> = krid(4, 3, null)
    assertThat(krid.isEmpty()).isTrue
    assertThat(krid.dimension.width).isEqualTo(4)
    assertThat(krid.dimension.height).isEqualTo(3)
  }

  @Test
  fun `get multiple values`() {
    val krid = booleanKrid(
      """
      tff
      ftt
      fft
    """.trimIndent()
    )

    assertThat(krid[listOf(cell(0, 0), cell(1, 2))])
      .containsExactly(
        cell(0, 0, true),
        cell(1, 2, false),
      )
  }

  @Test
  fun `get rows`() {
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
  fun `get columns`() {
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
  fun `iterate value cells`() {
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
  fun `set cell value`() {
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
  fun `fail to set cell values if cell out of bounds`() {
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

  @Test
  fun `subKrid - ul out of bounds`() {
    assertThatThrownBy {
      innerKrid.subKrid(cell(0, 7), cell(2, 2))
    }.isInstanceOf(IllegalArgumentException::class)
      .hasMessage("Cell(x=0, y=7) is out of bounds (dimension=Dimension(width=4, height=4)).")
  }

  @Test
  fun `subKrid - lr out of bounds`() {
    assertThatThrownBy {
      innerKrid.subKrid(cell(0, 0), cell(2, 7))
    }.isInstanceOf(IllegalArgumentException::class)
      .hasMessage("Cell(x=2, y=7) is out of bounds (dimension=Dimension(width=4, height=4)).")
  }

  @Test
  fun `subKrid - lr less than ur`() {
    assertThatThrownBy {
      innerKrid.subKrid(cell(2, 2), cell(1, 1))
    }.isInstanceOf(IllegalArgumentException::class)
      .hasMessage("Cell(x=1, y=1) has to be right and/or down from Cell(x=2, y=2).")
  }

  @Test
  fun `subKrid - success`() {
    with(innerKrid.subKrid(cell(1, 1), cell(2, 2))) {
      assertThat(dimension).isEqualTo(Dimension(2, 2))
      assertThat(row(0)).containsExactly(true, false)
      assertThat(row(1)).containsExactly(false, true)
    }
  }

  @Test
  fun `add two krids - defaults`() {
    val k1 = booleanKrid(
      """
      ...
      ttt
      ...
    """.trimIndent()
    )

    val k2 = booleanKrid(
      """
      tt
      ff
    """.trimIndent()
    ).toAddKrid()

    with(k1 + k2) {
      assertThat(row(0)).containsExactly(true, true, null)
      assertThat(row(1)).containsExactly(false, false, true)
      assertThat(row(2)).containsExactly(null, null, null)
    }
  }

  @Test
  fun `add two krids - with transformation`() {
    val k1 = booleanKrid(
      """
      ...
      ttf
      ...
    """.trimIndent()
    )

    val k2 = booleanKrid(
      """
      tt
      ff
    """.trimIndent()
    ).toAddKrid(offset = cell(1, 1)) { o, n -> (o ?: false) && (n ?: false) }

    with(k1 + k2) {
      assertThat(row(0)).containsExactly(null, null, null)
      assertThat(row(1)).containsExactly(true, true, false)
      assertThat(row(2)).containsExactly(null, false, false)
    }
  }

  @Test
  fun `add two krid - fail out of bounds`() {
    assertThatThrownBy {
      booleanKrid(
        """
        ..
        ..
      """.trimIndent()
      ) + booleanKrid(
        """
        tt
      """.trimIndent()
      ).toAddKrid(offset = cell(1, 0))
    }.isInstanceOf(IllegalArgumentException::class)
      .hasMessage("Cannot modify values because cells are out of bounds: [Cell(x=2, y=0)].")
  }

  @Test
  fun `adjacent cells `() {
    val krid: Krid<Int> = krid(
      """
      123
      456
      789
    """.trimIndent(), 0
    ) { it.toString().toInt() }

    assertThat(krid.adjacentCells(1, 1)).containsExactly(
      cell(1, 0), // up
      cell(2, 0), // up-right
      cell(2, 1), // right
      cell(2, 2), // down-right
      cell(1, 2), // down
      cell(0, 2),
      cell(0, 1),
      cell(0, 0),
    )

    assertThat(krid.adjacentCellValues(1, 1)).containsExactly(
      cell(1, 0, 2), // up
      cell(2, 0, 3), // up-right
      cell(2, 1, 6), // right
      cell(2, 2, 9), // down-right
      cell(1, 2, 8), // down
      cell(0, 2, 7),
      cell(0, 1, 4),
      cell(0, 0, 1),
    )


    assertThat(krid(false).adjacentCells(0, 0)).isEmpty()
  }


  @Test
  fun `orthogonalAdjacent cells`() {
    val krid: Krid<Char> = krid(
      """
      123
      456
      789
    """.trimIndent()
    )

    assertThat(krid.orthogonalAdjacentCells(1, 1)).containsExactly(
      cell(1, 0), // up
      cell(2, 1), // right
      cell(1, 2), // down
      cell(0, 1),
    )
    assertThat(Krids.krid(false).orthogonalAdjacentCells(0, 0)).isEmpty()
  }

  @Test
  fun `print ascii`() {
    val string = """
      abc
      def
    """.trimIndent()

    assertThat(krid(string).ascii()).isEqualTo(string)

    assertThat(krid(string).ascii { it.uppercaseChar() }).isEqualTo(
      """
      ABC
      DEF
    """.trimIndent()
    )
  }

  @Test
  fun `load from resource`() {
    val krid: Krid<Boolean> = krid(ResourceHelper.readFile("krid-ascii.txt"), false) { it == '#' }

    assertThat(krid.ascii()).isEqualTo(
      """
      fttffttfftttttfffttttttfftttttff
      fttfttfffttffttffffttffffttffttf
      fttttfffftttttfffffttffffttffttf
      fttfttfffttffttffffttffffttffttf
      fttffttffttffttffttttttfftttttff
      ffffffffffffffffffffffffffffffff
    """.trimIndent()
    )
  }

  @Test
  fun `getValue returns CellValue`() {
    val krid = krid(2, 3, false) { x, y -> x == y }

    assertThat(krid.getValue(0, 0))
      .isEqualTo(cell(0, 0, true))

    assertThat(krid.getValue(1, 2))
      .isEqualTo(cell(1, 2, false))
  }

  @Test
  fun `take a step in krid`() {
    val krid: Krid<Boolean> = krid(ResourceHelper.readFile("krid-ascii.txt"), false) { it == '#' }

    // resulting in DOWN(5) + RIGHT(4)
    val step = Direction.DOWN_RIGHT(5) + step(-1, 0)

    val value = krid.step(step)

    assertThat(value).isEqualTo(cell(4, 5, false))
  }

  @Test
  fun `take multiple steps in a krid`() {
    val krid: Krid<Boolean> = krid(ResourceHelper.readFile("krid-ascii.txt"), false) { it == '#' }
    // resulting in DOWN(2) + RIGHT(1)
    val step = Direction.DOWN_RIGHT(2) + step(-1, 0)

    // will only return 2 values, because sequence ends (isInBounds)
    val values = krid.steps(stepFn = step, number = 4)

    assertThat(values).containsExactlyInAnyOrder(
      cell(1, 2, true),
      cell(2, 4, true),
    )
  }

  @Test
  fun `cells and cell values`() {
    val krid = booleanKrid(
      """
      t.
      .f
    """.trimIndent()
    )

    assertThat(krid.cells().toList()).containsExactly(
      cell(0, 0),
      cell(1, 0),
      cell(0, 1),
      cell(1, 1),
    )

    assertThat(krid.cellValues().toList()).containsExactly(
      cell(0, 0, true),
      cell(1, 0, null),
      cell(0, 1, null),
      cell(1, 1, false),
    )
  }

  @Test
  fun `adjacent cells`() {
    val krid = booleanKrid(
      """
      tf.
      .tf
      f.t
    """.trimIndent()
    )

    assertThat(krid.adjacentCells(cell(0, 0))).containsExactlyInAnyOrder(
      cell(1, 0),
      cell(1, 1),
      cell(0, 1),
    )

    val c11 = cell(1, 1)
    assertThat(krid.adjacentCells(1, 1)).containsExactlyInAnyOrder(
      c11(Direction.UP),
      c11(Direction.UP_RIGHT),
      c11(Direction.RIGHT),
      c11(Direction.DOWN_RIGHT),
      c11(Direction.DOWN),
      c11(Direction.DOWN_LEFT),
      c11(Direction.LEFT),
      c11(Direction.UP_LEFT),
    )
  }

}
