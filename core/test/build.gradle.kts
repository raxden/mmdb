import extension.androidTestImplementationBundle
import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.test"
}

dependencies {

    implementationBundle(libs.bundles.test.asProvider())
    implementationBundle(libs.bundles.test.android)
    implementationBundle(libs.bundles.test.compose)
    implementationBundle(libs.bundles.test.espresso)
    implementationBundle(libs.bundles.test.coroutines)
    implementationBundle(libs.bundles.test.threetenabp)
    implementationBundle(libs.bundles.test.mockwebserver)
    implementationBundle(libs.bundles.test.hilt)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
