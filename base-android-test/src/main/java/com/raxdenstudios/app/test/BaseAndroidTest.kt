package com.raxdenstudios.app.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

abstract class BaseAndroidTest : KoinTest {

  @ExperimentalCoroutinesApi
  val testDispatcher = TestCoroutineDispatcher()

  @get:Rule
  val koinTestRule = KoinTestRule.create {
    modules(modules + module { factory { context } })
  }

  @get:Rule
  val timberTestRule: TimberTestRule = TimberTestRule.logAllAlways()

  protected val context: Context = InstrumentationRegistry.getInstrumentation().context
  protected abstract val modules: List<Module>
}
