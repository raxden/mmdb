plugins {
    id("com.raxdenstudios.android-compose-library")
}

android {
    namespace = "com.raxdenstudios.app.catalog"
}

dependencies {
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.coreUI))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreDomain))
    implementation(libs.commons.android)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.landscapists)
}
