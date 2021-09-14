package io.toolisticon.lib.krid

import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.Dimension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class KridTest {

  @Test
  internal fun `init fails - dimension#size ne list#size`() {
    assertThatThrownBy { Krid(Dimension(4, 5), true, List<Boolean>(40) { false }) }
      .isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  internal fun `create empty with dimension`() {
    val krid : Krid<Boolean?> = krid(4,3, null)
    assertThat(krid.isEmpty()).isTrue
  }



}
