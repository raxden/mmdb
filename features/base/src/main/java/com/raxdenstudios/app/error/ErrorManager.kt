package com.raxdenstudios.app.error

interface ErrorManager {
  fun handleError(throwable: Throwable)
}
