package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.Direction.DOWN
import io.toolisticon.lib.krid.model.step.Direction.RIGHT

fun main() {

  // create a 2dim-array of type Boolean where '.' is false and '#' is true
  val krid: Krid<Boolean> = Krids.krid(
    """
      .##..##..#####...######..#####..
      .##.##...##..##....##....##..##.
      .####....#####.....##....##..##.
      .##.##...##..##....##....##..##.
      .##..##..##..##..######..#####..
      ................................
    """.trimIndent(), false
  ) { it == '#' }

  // list all cells and values where cell is filled and x==y
  println(krid.sequence().filter { it.value && it.x == it.y }.toList())
  // [CellValue(x=1, y=1, value=true), CellValue(x=2, y=2, value=true)]

  // create a second krid representing the letter `O` programmatically
  val o: Krid<Boolean> = Krids.krid(8, 5, false)
    .plus(
      // upper line of O
      generateSequence(1) { it + 1 }
        .map { Krids.cell(it, 0, true) }
        .take(5).toList()
    ).plus(
      // lower line of O
      generateSequence(1) { it + 1 }
        .map { Krids.cell(it, 4, true) }
        .take(5).toList()
    ).plus(
      // left side of O
      generateSequence(0) { it + 1 }.map { Krids.cell(1, it, true) }.take(5).toList()
    ).plus(
      // right side of O
      generateSequence(0) { it + 1 }.map { Krids.cell(6, it, true) }.take(5).toList()
    )

  println(o.ascii())
  //    fttttttf
  //    ftfffftf
  //    ftfffftf
  //    ftfffftf
  //    fttttttf

  // add the second krid to the first with an offset of 16 columns, replacing letter `I`
  val sum = krid + o.toAddKrid(offset = Krids.cell(16, 0))

  // print `KROD` with '*'s instead of '#'s.
  println(sum.ascii { if (it) '*' else '.' })
  //   .**..**..*****...******..*****..
  //   .**.**...**..**..**..**..**..**.
  //   .****....*****...**..**..**..**.
  //   .**.**...**..**..**..**..**..**.
  //   .**..**..**..**..******..*****..
  //   ................................

  // jump like a chess knight, 2 right, 1 down
  val knightStep = RIGHT(2) + DOWN
  println(knightStep)
  // prints: RIGHT(2) + DOWN(1)

  // starting in top left
  val cellsInKnightsReach = knightStep.walk(Krids.cell(0,0))
    // until we leave the krid dimension
    .takeWhile { sum.dimension.isInBounds(it) }
    .map { CellValue(it, sum.get(it)) }
    .toList()
  println(cellsInKnightsReach)
}
