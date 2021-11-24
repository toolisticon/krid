package io.toolisticon.lib.krid._test

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.DirectionStep
import org.junit.jupiter.params.converter.TypedArgumentConverter

class StepConverter : TypedArgumentConverter<String, DirectionStep>(String::class.java, DirectionStep::class.java) {
  private val regex = """(\w+)\((\d+)\)""".toRegex()

  override fun convert(source: String): DirectionStep {
    val (directionName, numberValue) = requireNotNull(regex.find(source)).destructured

    return Direction.valueOf(directionName.trim()).invoke(numberValue.trim().toInt())
  }

}

class CellConverter : TypedArgumentConverter<String, Cell>(String::class.java, Cell::class.java) {

  override fun convert(source: String): Cell {
    val (x, y) = source.split(" to ")
    return cell(x.trim().toInt(), y.trim().toInt())
  }

}
