package com.raxdenstudios.commons

sealed class ResultData<out T> {

  data class Success<out T>(val value: T) : ResultData<T>()
  data class Error(val throwable: Throwable) : ResultData<Nothing>()
}

fun <T, R> ResultData<T>.map(transform: (value: T) -> R): ResultData<R> = when (this) {
  is ResultData.Error -> ResultData.Error(throwable)
  is ResultData.Success -> ResultData.Success(transform(value))
}

fun <T> ResultData<T>.getValueOrNull(): T? = when (this) {
  is ResultData.Error -> null
  is ResultData.Success -> value
}
