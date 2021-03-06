package io.toolisticon.lib.krid.model.step

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.CellConverter
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

class DirectionTest {

  @Test
  fun `directionStep to string`() {
    assertThat(LEFT(5))
      .hasToString("LEFT(5)")
  }

  @Test
  fun `can add stepFn to direction enum`() {
    assertThat(UP + DOWN(2))
      .hasToString("UP(1) + DOWN(2)")

    assertThat(UP + UP)
      .hasToString("UP(1) + UP(1)")

    assertThat(UP + (LEFT + UP))
      .hasToString("UP(1) + LEFT(1) + UP(1)")
  }

  @Test
  fun `beam down-right - include start`() {
    val beam = DOWN_RIGHT.beam(cell(0, 0), true)

    assertThat(beam.take(3).toList())
      .containsExactly(
        cell(0, 0),
        cell(1, 1),
        cell(2, 2),
      )
  }

  @Test
  fun `beam down-right - exclude start`() {
    val beam = DOWN_RIGHT.beam(cell(0, 0))

    assertThat(beam.take(3).toList())
      .containsExactly(
        cell(1, 1),
        cell(2, 2),
        cell(3, 3),
      )
  }

  @Test
  fun `none has length 0`() {
    assertThat(NONE.singleStep).isEqualTo(DirectionStep(NONE, 0))
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "UP, DOWN",
      "NONE, NONE",
      "UP, DOWN",
      "DOWN, UP",
      "LEFT, RIGHT",
      "RIGHT, LEFT",
      "UP_LEFT, DOWN_RIGHT",
      "DOWN_RIGHT, UP_LEFT",
      "UP_RIGHT, DOWN_LEFT",
      "DOWN_LEFT, UP_RIGHT",
    ]
  )
  fun `opposite directions`(direction: Direction, expected: Direction) {
    assertThat(direction.opposite).isEqualTo(expected)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "0 to 0, NONE",
      "0 to 5, DOWN",
      "0 to -4, UP",
      "3 to 0, RIGHT",
      "-2 to 0, LEFT",
      "3 to 3, DOWN_RIGHT",
      "-4 to -4, UP_LEFT",
      "-5 to 5, DOWN_LEFT",
      "7 to -7, UP_RIGHT",
    ]
  )
  fun `valueOf from coordinates`(
    @ConvertWith(CellConverter::class) coordinates: Cell,
    expected: Direction
  ) {
    assertThat(Direction.valueOf(coordinates)).isEqualTo(expected)
  }

}
