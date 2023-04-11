plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.test"
}

dependencies {
    implementation(libs.bundles.testing)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.testingUI)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
