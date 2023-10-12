import extension.androidTestImplementationBundle
import extension.debugImplementationBundle
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

    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.androidx.compose)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.hilt)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.coil.compose)
    implementationBundle(libs.bundles.retrofit2.asProvider())

    // debug libraries
    debugImplementationBundle(libs.bundles.debug.androidx.compose)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
    testImplementation(libs.bundles.test.threetenabp)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementationBundle(libs.bundles.test.android)
    androidTestImplementationBundle(libs.bundles.test.coroutines)
    androidTestImplementationBundle(libs.bundles.test.threetenabp)
    androidTestImplementationBundle(libs.bundles.test.androidx.compose)
    androidTestImplementationBundle(libs.bundles.test.hilt)
}
