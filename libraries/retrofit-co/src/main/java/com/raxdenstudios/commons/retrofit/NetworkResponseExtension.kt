package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData

inline fun <T : Any, U : Any, reified R : Any> NetworkResponse<T, U>.toResultData(
  errorMessage: String,
  transform: (value: T) -> R = { value -> value as R }
): ResultData<R> = when (this) {
  is NetworkResponse.Success -> ResultData.Success(transform(body))
  is NetworkResponse.ServerError -> when (code) {
    in 400..499 -> ResultData.Error(NetworkException.Client(code, errorMessage))
    in 500..599 -> ResultData.Error(NetworkException.Server(code, errorMessage))
    else -> ResultData.Error(NetworkException.Unknown(errorMessage))
  }
  is NetworkResponse.NetworkError -> ResultData.Error(NetworkException.Network(errorMessage, error))
  is NetworkResponse.UnknownError -> ResultData.Error(NetworkException.Unknown(errorMessage, error))
}
