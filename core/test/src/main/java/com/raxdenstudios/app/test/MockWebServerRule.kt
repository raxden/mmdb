package com.raxdenstudios.app.test

import androidx.test.platform.app.InstrumentationRegistry
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockWebServerRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                // Start the mock server before running a test
                RESTMockServerStarter.startSync(
                    AndroidAssetsFileParser(InstrumentationRegistry.getInstrumentation().context),
                    AndroidLogger(),
                )
                // Evaluate the current running test
                base.evaluate()
                // After the test reset the mock server
                RESTMockServer.reset()
            }
        }
    }
}
