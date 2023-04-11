plugins {
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.secrets)
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
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.coreModel))
    implementation(libs.commons.android)
    implementation(libs.commons.paginationCo)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
    implementation(libs.gson)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    debugImplementation(libs.gander.debug)
    releaseImplementation(libs.gander.release)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
