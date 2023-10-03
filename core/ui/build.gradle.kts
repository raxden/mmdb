import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-compose-library")
}

android {
    namespace = "com.raxdenstudios.app.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:i18n"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.androidx.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(platform(libs.firebase.bom))
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}
