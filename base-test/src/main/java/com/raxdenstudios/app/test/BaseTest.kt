package com.raxdenstudios.app.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raxdenstudios.commons.test.rules.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.koin.test.KoinTest

abstract class BaseTest : KoinTest {

  // Used for liveData testing purposes.
  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  val testDispatcher = TestCoroutineDispatcher()

  // Will override the Main dispatcher in test situations. This is helpful when you want to execute
  // a test in situations where the platform Main dispatcher is not available, or you wish to
  // replace Dispatchers.Main with a testing dispatcher.
  @ExperimentalCoroutinesApi
  @get:Rule
  val coroutineTestRule = CoroutineTestRule(testDispatcher)

  @get:Rule
  val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()
}
