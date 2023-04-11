plugins {
    id("com.raxdenstudios.android-compose-feature")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.raxdenstudios.app.feature.home"
}

dependencies {
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.corei18n))
    implementation(project(Modules.coreUI))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreDomain))
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
    androidTestImplementation(project(Modules.app))
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(project(Modules.coreNetwork))
    androidTestImplementation(project(Modules.coreDatabase))
    androidTestImplementation(libs.room.test)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
