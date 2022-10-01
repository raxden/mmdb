package com.raxdenstudios.app.test

import dagger.hilt.android.testing.CustomTestApplication

/**
 * If you cannot use HiltTestApplication because your test application needs to extend another
 * application, annotate a new class or interface with @CustomTestApplication, passing in the
 * value of the base class you want the generated Hilt application to extend.
 *
 * @CustomTestApplication will generate an Application class ready for testing with Hilt that
 * extends the application you passed as a parameter.
 *
 * More info:
 * https://developer.android.com/training/dependency-injection/hilt-testing#custom-application
 */
@CustomTestApplication(AppTestApplication::class)
interface HiltTestApplication
