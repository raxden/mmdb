import extension.androidTestImplementationBundle
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

    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.hilt)
    implementationBundle(libs.bundles.retrofit2.asProvider())

    // debug libraries
    debugImplementation(libs.gander.debug)

    // release libraries
    releaseImplementation(libs.gander.release)

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
    androidTestImplementationBundle(libs.bundles.test.hilt)
}
