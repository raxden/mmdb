plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.navigation"
}

dependencies {
    // libraries
    implementation(project(":core:common"))
    implementation(libs.bundles.threetenabp)
    implementation(libs.bundles.coroutines)
    implementation(libs.commons.android)
    implementation(libs.timber)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
