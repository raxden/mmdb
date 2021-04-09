package com.raxdenstudios.commons.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

private val onErrorStub: (e: Throwable) -> Unit = { _ -> }
private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
  Timber.e(throwable)
}

fun CoroutineScope.safeLaunch(
  exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
  block: suspend CoroutineScope.() -> Unit
): Job = this.launch(exceptionHandler) {
  block.invoke(this)
}

fun CoroutineScope.launch(
  onError: (throwable: Throwable) -> Unit = onErrorStub,
  block: suspend CoroutineScope.() -> Unit
): Job = this.launch(
  CoroutineExceptionHandler { _, throwable ->
    onError(throwable)
  }
) {
  block.invoke(this)
}
