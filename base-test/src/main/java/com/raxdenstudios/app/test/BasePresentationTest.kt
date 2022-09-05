package com.raxdenstudios.app.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule

@Deprecated("Use TestCoroutineRule instead")
abstract class BasePresentationTest {

    // Used for liveData testing purposes.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    // Will override the Main dispatcher in test situations. This is helpful when you want to execute
    // a test in situations where the platform Main dispatcher is not available, or you wish to
    // replace Dispatchers.Main with a testing dispatcher for example in viewmodels
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()
}
