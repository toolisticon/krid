package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.StepFn.Companion.step
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class CoordinatesStepTest {

  @Test
  fun `coordinatesStep can be constructed and invoked`() {
    var step = CoordinatesStep(1,1)
    assertThat(step.x).isEqualTo(1)
    assertThat(step.y).isEqualTo(1)

    assertThat(step(cell(1, 1)))
      .describedAs("1,1 -> no transformation")
      .isEqualTo(cell(2, 2))

    step += Direction.UP_LEFT(2)
    assertThat(step.x).isEqualTo(-1)
    assertThat(step.y).isEqualTo(-1)

    assertThat(step(cell(1, 1)))
      .describedAs("1,1 + -1,-1 = 0,0")
      .isEqualTo(cell(0, 0))
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "0 to 0, NONE(0)",
      "1 to 0, RIGHT(1)",
      "-2 to -3, LEFT(2) + UP(3)",
    ]
  )
  fun `convert to direction steps`(
    @ConvertWith(CellConverter::class) stepCell: Cell,
    expected: String
  ) {

    assertThat(step(stepCell).directionSteps.toString())
      .isEqualTo(expected)
  }

  @Test
  fun `toString contains coordinates without cell`() {
    assertThat(step(4,6).toString())
      .isEqualTo("CoordinatesStep(x=4, y=6)")
  }
}
