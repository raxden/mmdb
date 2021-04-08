package com.raxdenstudios.commons.util

import org.junit.Assert.assertEquals
import org.junit.Test

internal class DataMapperTest {

  private val dataMapper = object : DataMapper<Int, String>() {
    override fun transform(source: Int): String = source.toString()
  }

  @Test
  fun `Given a sort of integers, When transform is called, Then a sort of strings are returned`() {
    val listOfIntegers = listOf(1, 2, 3)

    val result = dataMapper.transform(listOfIntegers)

    assertEquals(
      listOf("1", "2", "3"),
      result
    )
  }
}
