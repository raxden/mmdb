plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-application")
    alias(libs.plugins.play.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.gradle.play.publisher)
    alias(libs.plugins.hilt.android)
}

versioning {
    versionFilePath = "$rootDir/version.properties"
}

play {
    track.set("beta")
}

android {
    namespace = "com.raxdenstudios.app"

    buildTypes {
        getByName("debug") {
            addManifestPlaceholders(mapOf("crashlyticsCollectionEnabled" to false))
            firebaseAppDistribution {
                serviceCredentialsFile = "$rootDir/config/service_credentials.json"
            }
        }
        getByName("release") {
            addManifestPlaceholders(mapOf("crashlyticsCollectionEnabled" to true))
            firebaseAppDistribution {
                serviceCredentialsFile = "$rootDir/config/service_credentials.json"
            }
        }
    }
}

dependencies {
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.corei18n))
    implementation(project(Modules.coreUI))
    implementation(project(Modules.coreNetwork))
    implementation(project(Modules.coreDatabase))
    implementation(project(Modules.coreDomain))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreData))
    implementation(project(Modules.coreNavigation))
    implementation(project(Modules.featureAccount))
    implementation(project(Modules.featureSearch))
    implementation(project(Modules.featureMediaList))
    implementation(project(Modules.featureMedia))
    implementation(project(Modules.featureHome))
    implementation(libs.commons.paginationCo)
    implementation(libs.bundles.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.threetenabp)
    implementation(libs.timber)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.landscapists)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(project(Modules.catalog))
    debugImplementation(libs.leakcanary)
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
