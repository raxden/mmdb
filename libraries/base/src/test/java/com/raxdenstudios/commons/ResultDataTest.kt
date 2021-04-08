package com.raxdenstudios.commons

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ResultDataTest {

  @Test
  fun `Given a success resultData, When getValue is called, Then return value`() {
    val resultData = givenASuccessResultData()

    val result = resultData.getValueOrNull()

    assertEquals("success", result)
  }

  @Test
  fun `Given a error resultData, When getValue is called, Then return null value`() {
    val resultData = givenAErrorResultData()

    val result = resultData.getValueOrNull()

    assertEquals(null, result)
  }

  @Test
  fun `Given a success resultData, When map is called, Then transformed data is returned`() {
    val resultData = givenASuccessResultData()

    val result = resultData.map { 1 }

    assertEquals(ResultData.Success(1), result)
  }

  @Test
  fun `Given a error resultData, When map is called, Then the same resultData is returned`() {
    val resultData = givenAErrorResultData()

    val result = resultData.map { 1 }

    result as ResultData.Error

    assert(result.throwable is IllegalStateException)
    assertEquals("illegalStateException", result.throwable.message)
  }

  private fun givenAErrorResultData(): ResultData<String> =
    ResultData.Error(IllegalStateException("illegalStateException"))

  private fun givenASuccessResultData(): ResultData<String> =
    ResultData.Success("success")
}
