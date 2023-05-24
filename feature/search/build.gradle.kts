plugins {
    id("com.raxdenstudios.android-compose-feature")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.search"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:i18n"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(libs.commons.android)
    implementation(libs.commons.paginationCo)
    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.landscapists)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(platform(libs.firebase.bom))
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
