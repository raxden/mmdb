import extension.androidTestImplementationBundle
import extension.debugImplementationBundle
import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-compose-feature")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.feature.home"

    defaultConfig {
        testInstrumentationRunner = "com.raxdenstudios.app.test.AppTestRunner"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:i18n"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.androidx.compose)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.hilt)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.coil.compose)

    // debug libraries
    debugImplementationBundle(libs.bundles.debug.androidx.compose)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
    testImplementation(libs.bundles.test.threetenabp)

    // instrumental test libraries
    androidTestImplementation(project(":app"))
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(project(":core:network"))
    androidTestImplementation(project(":core:database"))
    androidTestImplementationBundle(libs.bundles.test.android)
    androidTestImplementationBundle(libs.bundles.test.coroutines)
    androidTestImplementationBundle(libs.bundles.test.threetenabp)
    androidTestImplementationBundle(libs.bundles.test.espresso)
    androidTestImplementationBundle(libs.bundles.test.androidx.compose)
    androidTestImplementationBundle(libs.bundles.test.hilt)
    androidTestImplementationBundle(libs.bundles.test.mockwebserver)
    androidTestImplementationBundle(libs.bundles.test.room)
}
