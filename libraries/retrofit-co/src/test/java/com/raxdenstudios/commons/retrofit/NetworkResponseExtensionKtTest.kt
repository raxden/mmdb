package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData
import okhttp3.Headers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

internal class NetworkResponseExtensionKtTest {

  @Test
  fun `Given a client error with code 4xx, When toResult is called, Then a ResultData_Error with NetworkException_Client is returned`() {
    val networkResponse = givenANetworkServerErrorResponse(400)

    val result = networkResponse.toResultData("message") { }

    assertEquals(
      ResultData.Error(NetworkException.Client(400, "message")),
      result
    )
  }

  @Test
  fun `Given a server error with code 5xx, When toResult is called, Then a ResultData_Error with NetworkException_Server is returned`() {
    val networkResponse = givenANetworkServerErrorResponse(500)

    val result = networkResponse.toResultData("message") { }

    assertEquals(
      ResultData.Error(NetworkException.Server(500, "message")),
      result
    )
  }

  @Test
  fun `Given a network error, When toResult is called, Then a ResultData_Error with NetworkException_Network is returned`() {
    val networkResponse = givenANetworkErrorResponse()

    val result = networkResponse.toResultData("message") { }

    result as ResultData.Error

    assert(result.throwable is NetworkException.Network)
    assert(result.throwable.cause is IOException)
  }

  @Test
  fun `Given a unknown error, When toResult is called, Then a ResultData_Error with NetworkException_Unknown is returned`() {
    val networkResponse = givenAUnknownErrorResponse()

    val result = networkResponse.toResultData("message") { }

    result as ResultData.Error

    assert(result.throwable is NetworkException.Unknown)
    assert(result.throwable.cause is Throwable)
  }
}

private fun givenANetworkServerErrorResponse(code: Int) = NetworkResponse.ServerError(
  body = "",
  code = code,
  headers = Headers.of(emptyMap()),
)

private fun givenANetworkErrorResponse() = NetworkResponse.NetworkError(
  error = IOException(""),
)

private fun givenAUnknownErrorResponse() = NetworkResponse.UnknownError(
  error = Throwable(""),
)
