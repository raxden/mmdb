import extension.roomConfig
import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.database"

    roomConfig(project)
}

dependencies {
    // libraries
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementationBundle(libs.bundles.threetenabp)

    implementationBundle(libs.bundles.hilt)
    kapt(libs.hilt.compiler)

    implementationBundle(libs.bundles.room)
    kapt(libs.room.compiler)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}
