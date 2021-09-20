package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid._test.StepConverter
import io.toolisticon.lib.krid.model.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class StepsTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "UP(1), 5 to 4",
      "UP(3), 5 to 2",
      "UP_RIGHT(1), 6 to 4",
      "UP_RIGHT(3), 8 to 2",
      "RIGHT(1), 6 to 5",
      "RIGHT(3), 8 to 5",
      "DOWN_RIGHT(1), 6 to 6",
      "DOWN_RIGHT(3), 8 to 8",
      "DOWN(1), 5 to 6",
      "DOWN(3), 5 to 8",
      "DOWN_LEFT(1), 4 to 6",
      "DOWN_LEFT(3), 2 to 8",
      "LEFT(1), 4 to 5",
      "LEFT(3), 2 to 5",
      "UP_LEFT(1), 4 to 4",
      "UP_LEFT(3), 2 to 2",
    ]
  )
  internal fun `evaluate step`(@ConvertWith(StepConverter::class) step: Step, @ConvertWith(CellConverter::class) expectedCell: Cell) {
    val cell = cell(5, 5)

    assertThat(cell(step)).isEqualTo(expectedCell)
  }

  @Test
  internal fun `step to string`() {
    assertThat(LEFT(5))
      .hasToString("LEFT(5)")
  }

  @Test
  internal fun `can create steps from direction`() {
    var steps = UP + LEFT
    assertThat(steps).hasToString("UP(1) + LEFT(1)")

    steps += DOWN(2)
    assertThat(steps).hasToString("UP(1) + LEFT(1) + DOWN(2)")
  }

  @Test
  internal fun `can add stepFn to direction enum`() {
    assertThat(UP + DOWN(2))
      .hasToString("UP(1) + DOWN(2)")
    assertThat(UP + UP)
      .hasToString("UP(1) + UP(1)")
    assertThat(UP + (LEFT + UP))
      .hasToString("UP(1) + LEFT(1) + UP(1)")
  }

  @Test
  internal fun `single step`() {
    assertThat(cell(2, 2)(UP_LEFT(2))).isEqualTo(cell(0, 0))
  }

  @Test
  internal fun `take multiple steps in directions`() {
    val cell = Cell(2, 3)

    val steps: Steps = UP + (UP + RIGHT(2)) + UP_RIGHT

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

  @Test
  internal fun `beam down-right - include start`() {
    val beam = DOWN_RIGHT.beam(cell(0, 0), true)

    assertThat(beam.take(3).toList())
      .containsExactly(
        cell(0, 0),
        cell(1, 1),
        cell(2, 2),
      )
  }

  @Test
  internal fun `beam down-right - exclude start`() {
    val beam = DOWN_RIGHT.beam(cell(0, 0))

    assertThat(beam.take(3).toList())
      .containsExactly(
        cell(1, 1),
        cell(2, 2),
        cell(3, 3),
      )
  }
}
