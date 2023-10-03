import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.core.domain"
}

dependencies {
    // libraries
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}
