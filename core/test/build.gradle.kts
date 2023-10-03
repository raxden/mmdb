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

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
