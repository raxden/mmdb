import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.test"
}

dependencies {
    implementationBundle(libs.bundles.test.android)
    implementationBundle(libs.bundles.test.coroutines)
    implementationBundle(libs.bundles.test.threetenabp)
    implementationBundle(libs.bundles.test.espresso)
    implementationBundle(libs.bundles.test.androidx.compose)
    implementationBundle(libs.bundles.hilt)
    implementationBundle(libs.bundles.test.hilt)
    implementationBundle(libs.bundles.test.mockwebserver)
}
