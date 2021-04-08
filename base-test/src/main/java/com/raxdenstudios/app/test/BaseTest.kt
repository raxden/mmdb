package com.raxdenstudios.app.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raxdenstudios.commons.test.rules.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

abstract class BaseTest : KoinTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule() // Used for liveData testing purposes.

  @get:Rule
  val koinTestRule = KoinTestRule.create { modules(modules) }

  @ExperimentalCoroutinesApi
  @get:Rule
  val coroutineTestRule = CoroutineTestRule()

  @get:Rule
  val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

  protected abstract val modules: List<Module>
}
