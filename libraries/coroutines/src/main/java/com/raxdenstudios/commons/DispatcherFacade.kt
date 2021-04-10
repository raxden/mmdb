package com.raxdenstudios.commons

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherFacade {

  fun io(): CoroutineDispatcher
  fun default(): CoroutineDispatcher
  fun main(): CoroutineDispatcher
}