package com.raxdenstudios.commons.test.rules

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule(
  private val testDispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

  override fun starting(description: Description?) {
    super.starting(description)
    Dispatchers.setMain(testDispatcher)
  }

  override fun finished(description: Description?) {
    super.finished(description)
    Dispatchers.resetMain()
    when (testDispatcher) {
      is TestCoroutineDispatcher -> testDispatcher.cleanupTestCoroutines()
    }
  }
}
