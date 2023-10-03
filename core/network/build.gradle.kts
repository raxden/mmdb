import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.gradle.secrets)
}

secrets {
    // Optionally specify a different file name containing your secrets. The plugin defaults to "local.properties"
    propertiesFileName = "./config/secrets.properties"

    // A properties file containing default secret values. This file can be checked in version control.
    defaultPropertiesFileName = "./config/secrets.defaults.properties"
}

android {
    namespace = "com.raxdenstudios.app.core.network"
}

dependencies {
    // libraries
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.retrofit.asProvider())
    implementation(libs.network.response.adapter)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.gander.debug)
    releaseImplementation(libs.gander.release)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}
