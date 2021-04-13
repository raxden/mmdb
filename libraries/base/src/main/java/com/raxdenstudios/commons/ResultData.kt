package com.raxdenstudios.commons

sealed class ResultData<out T> {

  data class Success<out T>(val value: T) : ResultData<T>()
  data class Error(val throwable: Throwable) : ResultData<Nothing>()
}

fun <T, R> ResultData<T>.map(function: (value: T) -> R): ResultData<R> = when (this) {
  is ResultData.Error -> ResultData.Error(throwable)
  is ResultData.Success -> ResultData.Success(function(value))
}

suspend fun <T, R> ResultData<T>.coMap(function: suspend (value: T) -> R): ResultData<R> =
  when (this) {
    is ResultData.Error -> ResultData.Error(throwable)
    is ResultData.Success -> ResultData.Success(function(value))
  }

fun <T, R> ResultData<T>.flatMap(function: (value: T) -> ResultData<R>): ResultData<R> =
  when (this) {
    is ResultData.Error -> ResultData.Error(throwable)
    is ResultData.Success -> function(value)
  }

suspend fun <T, R> ResultData<T>.coFlatMap(function: suspend (value: T) -> ResultData<R>): ResultData<R> =
  when (this) {
    is ResultData.Error -> ResultData.Error(throwable)
    is ResultData.Success -> function(value)
  }

fun <T> ResultData<T>.getValueOrNull(): T? = when (this) {
  is ResultData.Error -> null
  is ResultData.Success -> value
}

fun <T> ResultData<T>.getValueOrDefault(default: T): T = when (this) {
  is ResultData.Error -> default
  is ResultData.Success -> value
}

fun <T> ResultData<T>.fold(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Unit =
  when (this) {
    is ResultData.Error -> onError(throwable)
    is ResultData.Success -> onSuccess(value)
  }

fun <T> ResultData<T>.onSuccess(function: (success: T) -> Unit): ResultData<T> = when (this) {
  is ResultData.Error -> this
  is ResultData.Success -> also { function(value) }
}

fun <T> ResultData<T>.onFailure(function: (failure: Throwable) -> Unit): ResultData<T> =
  when (this) {
    is ResultData.Error -> also { function(throwable) }
    is ResultData.Success -> this
  }