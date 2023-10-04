import extension.androidTestImplementationBundle
import extension.implementationBundle

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

    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.androidx.compose)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.hilt)

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
}
