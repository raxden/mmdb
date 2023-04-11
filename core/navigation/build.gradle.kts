plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.navigation"
}

dependencies {
    // libraries
    implementation(project(Modules.coreCommon))
    implementation(libs.bundles.threetenabp)
    implementation(libs.bundles.coroutines)
    implementation(libs.commons.android)
    implementation(libs.timber)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
