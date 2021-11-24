package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid._test.StepConverter
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction.UP_LEFT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class DirectionStepTest {
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
  fun `evaluate step`(@ConvertWith(StepConverter::class) step: DirectionStep, @ConvertWith(CellConverter::class) expectedCell: Cell) {
    val cell = cell(5, 5)

    assertThat(cell(step))
      .describedAs("Starting at $cell go $step to reach $expectedCell")
      .isEqualTo(expectedCell)
  }

  @Test
  fun `single step`() {
    assertThat(cell(2, 2)(UP_LEFT(2)))
      .isEqualTo(cell(0, 0))
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "LEFT(1), RIGHT(1)",
      "UP_LEFT(7), DOWN_RIGHT(7)",
      "NONE(0), NONE(0)",
    ]
  )
  fun `opposite direction`(@ConvertWith(StepConverter::class) step: DirectionStep, @ConvertWith(StepConverter::class) expected: DirectionStep) {
    assertThat(step.opposite).isEqualTo(expected)
  }


  @ParameterizedTest
  @CsvSource(
    value = [
      "-9, LEFT(9)",
      "7, RIGHT(7)",
      "0, NONE(0)",
    ]
  )
  fun `horizontal from number`(number: Int, @ConvertWith(StepConverter::class) expected: DirectionStep) {
    assertThat(DirectionStep.horizontal(number)).isEqualTo(expected)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "-9, UP(9)",
      "7, DOWN(7)",
      "0, NONE(0)",
    ]
  )
  fun `vertical from number`(number: Int, @ConvertWith(StepConverter::class) expected: DirectionStep) {
    assertThat(DirectionStep.vertical(number)).isEqualTo(expected)
  }


}
