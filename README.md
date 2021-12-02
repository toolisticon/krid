# krid

Your one-stop library to support two dimensional **K**otlin g**RID**s.

[![Build Status](https://github.com/toolisticon/krid/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/krid/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.lib/krid/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.lib/krid)

## Documentation

```ascii
.##..##..#####...######..#####..
.##.##...##..##....##....##..##.
.####....#####.....##....##..##.
.##.##...##..##....##....##..##.
.##..##..##..##..######..#####..
................................
```

see it in action here: [KridReadme](./src/test/kotlin/KridReadme.kt)

```kotlin

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
  val krod = krid + o.toAddKrid(offset = Krids.cell(16, 0))

  // print `KROD` with '*'s instead of '#'s.
  println(krod.ascii { if (it) '*' else '.' })
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
  val cellsInKnightsReach = krod.walk(stepFn = knightStep)
    .toList()
  println(cellsInKnightsReach)
  // prints: [CellValue(x=2, y=1, value=true), CellValue(x=4, y=2, value=true), CellValue(x=6, y=3, value=false), CellValue(x=8, y=4, value=false), CellValue(x=10, y=5, value=false)]
```



## Motivation

Two dimensional grids are everywhere. Whether you render a table, find possible moves on a chessboard, implement a tetris game, navigate santa clause through a maze of `#` and `.` characters during [AoC](https://adventofcode.com/), ... I had dozens of use cases in the past where I had to identify and move cells in a 2D array. But ... it's not a use case natively  supported by my favorite programming language.

Sure, it's not a "hard" task to store values in a list or array and use width/height attributes to correctly address elements by their `x,y`-coordinates, but it is a repetitive and error prone task that has to be implemented over and over again. 

So here is (the) solution: **krid** - your friendly neighborhoods kotlin library for creating, calculating, expanding and travelling two-dimensional arrays (aka grids) with kotlin.

### Naming things

This lib follows the tradition that kotlin libs might also just start with the letter `k` and since my first motivation for creating this where boardgame-implementations, I liked that according to  [wisdomlib.org](https://www.wisdomlib.org/definition/krid), `krid` in the Sanskrit dictionary means: _1) To play, amuse oneself, 2) To gamble, play at dice 3) To jest, joke or trifle with_ 

_Disclaimer_: This lib is not in any way related to the seemingly popular data analysis project [kgrid](https://github.com/kgrid). 
<br/>I was aware of [Vincent Carriers kgrid library](https://github.com/Vincent-Carrier/kgrid) while writing krid, though. It was for sure an inspiration, although, putting the same vocabulary aside, I took a different approach using immutable and sealed base classes.

### Scope

Although you could use a krid of type Number to do mathematical matrix operations, there might be other libs out there that will be more useful. Keep in mind that krid focusses on addressing cells, navigating via steps and transforming by adding and substracting (sub-)krids, basically just setting the foundation for things to build on top.

### Design Decisions

* This lib is designed to be as usable as possible, without being too invasive to your code base and dependency tree. Consequentially, it does not require any transitive dependencies.<br/>It just uses the core features provided by **JDK 11.x** and **[Kotlin 1.6.x](https://blog.jetbrains.com/kotlin/2021/11/kotlin-1-6-0-is-released/)**.

* Immutability - modification of Krids is not possible, there are no setters for any attributes, instead, whenever something has to be changed, a new copy is created containing the new state.

* Operator overloading - instead of coming up with clever function names, wherever possible `krid` uses [operator overloads](https://kotlinlang.org/docs/operator-overloading.html)  for composing new objects
