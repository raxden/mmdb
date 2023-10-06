import extension.androidTestImplementationBundle
import extension.implementationBundle
import extension.roomSetup

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.database"

    roomSetup(project)
}

dependencies {
    // libraries
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.room)
    implementationBundle(libs.bundles.hilt)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
    testImplementation(libs.bundles.test.threetenabp)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementationBundle(libs.bundles.test.android)
    androidTestImplementationBundle(libs.bundles.test.threetenabp)
    androidTestImplementationBundle(libs.bundles.test.room)
    androidTestImplementationBundle(libs.bundles.test.hilt)
}
