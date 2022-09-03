package com.raxdenstudios.app.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

abstract class BaseAndroidTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(modules + module { factory { context } })
    }

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllAlways()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().context
    protected abstract val modules: List<Module>
}
