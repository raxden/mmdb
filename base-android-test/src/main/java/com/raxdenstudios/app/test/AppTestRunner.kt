package com.raxdenstudios.app.test

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import io.appflate.restmock.RESTMockOptions
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger

internal class AppTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, AppTestApplication::class.java.name, context)

    override fun onCreate(arguments: Bundle) {
        super.onCreate(arguments)

        RESTMockServerStarter.startSync(
            AndroidAssetsFileParser(context),
            AndroidLogger(),
            RESTMockOptions.Builder().useHttps(true).build()
        )
    }
}
