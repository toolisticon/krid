package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.ORIGIN
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.*
import io.toolisticon.lib.krid.model.step.StepFn
import io.toolisticon.lib.krid.model.step.generateSequence

/**
 * A [Krid] of type `<E>` with given [Dimension].
 *
 * @property dimension width/height dimension
 * @property emptyElement how to represent an empty cell
 * @property list internal storage
 */
data class Krid<E>(
  override val dimension: Dimension,
  override val emptyElement: E,
  override val list: List<E>
) : AbstractKrid<E>() {

  init {
    // dimension must match the size of the internal list.
    require(dimension.size == list.size) { "$dimension does not match internal size=${list.size}." }
  }

  private val rowCache: MutableMap<Int, Row<E>> = mutableMapOf()
  private val columnCache: MutableMap<Int, Column<E>> = mutableMapOf()

  val width = dimension.width
  val height = dimension.height

  override fun row(index: Int): Row<E> = rowCache.computeIfAbsent(
    requireInRows(index)
  ) { Row(index, dimension.columnRange.map { get(it, index) }) }

  override fun column(index: Int): Column<E> = columnCache.computeIfAbsent(
    requireInColumns(index)
  ) { Column(index, dimension.rowRange.map { get(index, it) }) }

  operator fun plus(cellValues: List<CellValue<E>>): Krid<E> {
    dimension.filterNotInBounds(cellValues.cells).also {
      require(it.isEmpty()) { "Cannot modify values because cells are out of bounds: $it." }
    }

    val mutable = list.toMutableList()

    cellValues.forEach {
      mutable[indexTransformer.toIndex(it.cell)] = it.value
    }
    return copy(
      list = mutable.toList()
    )
  }

  operator fun plus(add: AddKrid<E>): Krid<E> {
    dimension.filterNotInBounds(add.cells).also {
      require(it.isEmpty()) { "Cannot modify values because cells are out of bounds: $it." }
    }

    val values: List<CellValue<E>> = add.cellValues.map {
      val old: E = this[it.cell]

      cell(it.cell, add.operation(old, it.value))
    }

    return this + values
  }

  fun subKrid(upperLeft: Cell, lowerRight: Cell): Krid<E> {
    require(dimension.isInBounds(upperLeft)) { "$upperLeft is out of bounds (dimension=$dimension)." }
    require(dimension.isInBounds(lowerRight)) { "$lowerRight is out of bounds (dimension=$dimension)." }

    val newDimension = Dimension(upperLeft, lowerRight)

    return krid(
      dimension = newDimension,
      emptyElement = emptyElement,
      initialize = { x, y ->
        get(x + upperLeft.x, y + upperLeft.y)
      }
    )
  }
}

operator fun <E> Krid<E>.plus(cellValue: CellValue<E>): Krid<E> = plus(listOf(cellValue))

fun <E> Krid<E>.step(stepFn: StepFn, start: Cell = ORIGIN): CellValue<E> = this.getValue(stepFn.invoke(start))

fun <E> Krid<E>.walk(stepFn: StepFn, start: Cell = ORIGIN, includeStart: Boolean = false): Sequence<CellValue<E>> = stepFn.generateSequence(start, includeStart)
  .takeWhile { this.dimension.isInBounds(it) }
  .map { this.getValue(it) }

fun <E> Krid<E>.steps(stepFn: StepFn, start: Cell = ORIGIN, number: Int): List<CellValue<E>> = walk(stepFn= stepFn, start= start, includeStart = false).take(number).toList()

fun <E> Krid<E>.plus(offset: Cell, krid: Krid<E>, operation: (E, E) -> E = { _, new -> new }) = plus(krid.toAddKrid(offset, operation))

/**
 * Create [AddKrid] of given krid.
 */
fun <E> Krid<E>.toAddKrid(offset: Cell = cell(0, 0), operation: (E, E) -> E = { _, new -> new }) =
  AddKrid(offset = offset, krid = this, operation = operation)
