package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.Cell
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource


internal class CompositeStepTest {

  @Test
  fun `can create steps from direction`() {
    var steps = Direction.UP + Direction.LEFT
    assertThat(steps).hasToString("UP(1) + LEFT(1)")

    steps += Direction.DOWN(2)
    assertThat(steps).hasToString("UP(1) + LEFT(1) + DOWN(2)")
  }

  @Test
  fun `take multiple steps in directions`() {
    val cell = Cell(2, 3)

    val steps: CompositeStep = Direction.UP + (Direction.UP + Direction.RIGHT(2)) + Direction.UP_RIGHT

    val all = steps.walk(cell)

    assertThat(all.toList()).containsExactly(
      cell(2, 3),
      cell(2, 2),
      cell(2, 1),
      cell(4, 1),
      cell(5, 0),
    )

    // this returns only the final cell
    assertThat(cell(steps)).isEqualTo(cell(5, 0))
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "1 to 0, false, RIGHT(1)",
      "-5 to 0, false, LEFT(5)",
    ]
  )
  fun `orthogonal steps from coordinates`(
    @ConvertWith(CellConverter::class) coordinates: Cell,
    optimize:Boolean,
    expected: String
  ) {
    assertThat(CompositeStep(coordinates, optimize).toString())
      .isEqualTo(expected)
  }
}
