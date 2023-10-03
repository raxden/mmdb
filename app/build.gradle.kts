import extension.implementationBundle

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
    filePath = "$rootDir/version.properties"
}

play {
    track.set("beta")
}

android {
    namespace = "com.raxdenstudios.app"

    defaultConfig {
        applicationId = "com.raxdenstudios.mmdb"

        // apk name, is posible to add variables as version, date...
        project.setProperty("archivesBaseName", "mmdb")
    }

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
    implementation(project(":core:common"))
    implementation(project(":core:i18n"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:account"))
    implementation(project(":feature:search"))
    implementation(project(":feature:list"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:home"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.coil.compose)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.androidx.compose)

    implementationBundle(libs.bundles.hilt)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(project(":catalog"))
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.espresso)
    androidTestImplementation(libs.bundles.test.coroutines)
    androidTestImplementation(libs.bundles.test.threetenabp)
    androidTestImplementation(libs.bundles.test.mockwebserver)
    androidTestImplementation(libs.bundles.test.hilt)
    kaptAndroidTest(libs.hilt.compiler)
}
