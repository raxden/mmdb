package com.raxdenstudios.app.test

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner

// https://developer.android.com/training/dependency-injection/hilt-testing#custom-application
class AppTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, HiltTestApplication_Application::class.java.name, context)

    override fun onCreate(arguments: Bundle) {
        super.onCreate(arguments)
    }
}
