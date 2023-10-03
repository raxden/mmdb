import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-compose-feature")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.feature.home"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:i18n"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.androidx.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
    kaptAndroidTest(libs.hilt.compiler)
}
