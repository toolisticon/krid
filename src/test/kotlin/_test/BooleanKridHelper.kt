package io.toolisticon.lib.krid._test

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.CellValue

object BooleanKridHelper {

  fun booleanKrid(string: String) = krid(
    string,
    null,
    parseChar
  )

  fun booleanCellValue(x:Int, y:Int, value:Boolean?): CellValue<Boolean?> = cell(x,y,value)

  val parseChar: (Char) -> Boolean? = {
    when (it) {
      't' -> true
      'f' -> false
      '.' -> null
      else -> throw IllegalArgumentException("Cannot map '$it'")
    }
  }

  val toChar01: (Boolean?) -> Char = {
    when (it) {
      true -> '1'
      false -> '0'
      else -> '.'
    }
  }

}
