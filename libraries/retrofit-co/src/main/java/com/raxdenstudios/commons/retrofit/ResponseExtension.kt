package com.raxdenstudios.commons.retrofit

import com.raxdenstudios.commons.ResultData
import retrofit2.Response
import java.io.IOException

fun <T : Any> Response<T>.toResultData(
  exceptionMessage: String
): ResultData<T> {
  if (isSuccessful) {
    try {
      val body = body()
      if (body != null)
        return ResultData.Success(body)
    } catch (e: Exception) {
      return ResultData.Error(IOException(exceptionMessage))
    }
  }
  return ResultData.Error(IOException(exceptionMessage))
}

fun <T : Any, R : Any> Response<T>.toResultData(
  exceptionMessage: String,
  map: (T) -> R
): ResultData<R> {
  if (isSuccessful) {
    try {
      val body = body()
      if (body != null)
        return ResultData.Success(map(body))
    } catch (e: Exception) {
      return ResultData.Error(IOException(exceptionMessage, e))
    }
  }
  return ResultData.Error(IOException(exceptionMessage))
}
