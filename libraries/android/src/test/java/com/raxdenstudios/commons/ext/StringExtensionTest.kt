package com.raxdenstudios.commons.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtensionTest {

  @Test
  fun `Given a random text, When toMD5 extension is called, Then random text is codified to MD5`() {
    val result = "Generando un MD5 de un texto".toMD5()

    assertEquals("5df9f63916ebf8528697b629022993e8", result)
  }
}
