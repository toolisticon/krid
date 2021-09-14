package io.toolisticon.lib.krid._test

import io.toolisticon.lib.krid.Krids.krid

object BooleanKridHelper {

  fun booleanKrid(string: String) = krid(
    string,
    null,
    parseChar
  )

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
