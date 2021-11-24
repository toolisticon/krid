package io.toolisticon.lib.krid.model

import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid._test.BooleanKridHelper.booleanKrid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class AddKridTest {

  private val krid = booleanKrid(
    """
      tf
      f.
    """.trimIndent()
  )

  @Test
  fun `create with defaults`() {
    val add: AddKrid<Boolean?> = AddKrid(krid = krid)

    assertThat(add.cells).containsExactly(
      cell(0, 0),
      cell(1, 0),
      cell(0, 1),
      cell(1, 1),
    )

    assertThat(add.cellValues).containsExactly(
      cell(0, 0, true),
      cell(1, 0, false),
      cell(0, 1, false),
      cell(1, 1, null),
    )

    assertThat(add[cell(1, 1)]).isEqualTo(null)

    assertThat(add(cell(0, 0, false))).isEqualTo(cell(0, 0, true))

    assertThat(add[cell(1, 0)]).isEqualTo(false)
  }

  @Test
  fun `create with offset`() {
    val add: AddKrid<Boolean?> = AddKrid(
      krid = krid,
      offset = cell(1, 1)
    )

    assertThat(add.cells).containsExactly(
      cell(1, 1),
      cell(2, 1),
      cell(1, 2),
      cell(2, 2),
    )

    assertThat(add.cellValues).containsExactly(
      cell(1, 1, true),
      cell(2, 1, false),
      cell(1, 2, false),
      cell(2, 2, null),
    )

    // this effectively gets 0,0
    assertThat(add[cell(1, 1)]).isEqualTo(true)

    // this effectively updates 1,1
    assertThat(add(cell(2, 2, false)))
      .isEqualTo(cell(2, 2, null))
  }


}
