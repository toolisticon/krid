package  io.toolisticon.lib.krid.fn

import io.toolisticon.lib.krid.model.Cell

/**
 * Calculates cell coordinates and list index based on width.
 */
class IndexTransformer(private val width: Int) {

  /**
   * Given the width of the Krid, this calculates the Cell(x,y) coordinates based on the list index.
   *
   * @param index the index in a list
   * @return cell coordinates in list
   */
  fun toCell(index: Int): Cell = Cell(index % width, index / width)

  /**
   * Given the width of the Krid, this calculates the the list index value  based on the Cell(x,y) coordinates.
   *
   * @param cell the (x,y) coordinates wrapped in a cell
   * @return index in list
   */
  fun toIndex(cell: Cell): Int = toIndex(cell.x, cell.y)

  /**
   * Given the width of the Krid, this calculates the the list index value  based on the (x,y) coordinates.
   *
   * @param x x coordinate
   * @param y y coordinate
   * @return index in list
   */
  fun toIndex(x: Int, y: Int): Int = y * width + x

}
