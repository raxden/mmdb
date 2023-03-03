package com.raxdenstudios.app.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ThreeTenRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                initThreeTen()
                base.evaluate()
            }
        }
    }
}
