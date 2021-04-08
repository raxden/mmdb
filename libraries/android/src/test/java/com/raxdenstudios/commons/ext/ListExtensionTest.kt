package com.raxdenstudios.commons.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ListExtensionTest {

  @Test
  fun `Given a sort of integers, When replaceItem extension is called, Then the value selected is replaced`() {
    val someItems = listOf(1, 2, 3)

    val result = someItems.replaceItem(4) { itemToReplace -> itemToReplace == 1 }

    assertEquals(listOf(4, 2, 3), result)
  }
}
