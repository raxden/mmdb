plugins {
    id("com.raxdenstudios.android-compose-library")
}

android {
    namespace = "com.raxdenstudios.app.core.ui"
}

dependencies {
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.corei18n))
    implementation(project(Modules.coreDomain))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreNavigation))
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.threetenabp)
    implementation(libs.commons.paginationCo)
    implementation(libs.commons.android)
    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.landscapists)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(platform(libs.firebase.bom))
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
