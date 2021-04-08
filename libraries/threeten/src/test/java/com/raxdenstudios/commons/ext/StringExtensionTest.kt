package com.raxdenstudios.commons.ext

import com.raxdenstudios.commons.threeten.ext.toLocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate

internal class StringExtensionTest {

  @Test
  fun `Given a simple date and a sort of possible patterns, When toLocalDate is called, Then return a valid LocalDate`() {
    val aValidDate = "01/01/2000"
    val aSortOfPatterns = listOf("dd/MM/yyyy", "dd-MM-yyyy").toTypedArray()

    val result = aValidDate.toLocalDate(*aSortOfPatterns)

    assertEquals(
      LocalDate.of(2000, 1, 1),
      result
    )
  }

  @Test
  fun `Given a simple date and a sort of possible patterns, When toLocalDate is called and not found any valid patterm, Then return null`() {
    val aValidDate = "01/01/2000"
    val aSortOfPatterns = listOf("dd--MM--yyyy", "dd-MM-yyyy").toTypedArray()

    val result = aValidDate.toLocalDate(*aSortOfPatterns)

    assertEquals(
      null,
      result
    )
  }
}

