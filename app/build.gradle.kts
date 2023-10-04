import extension.androidTestImplementationBundle
import extension.debugImplementationBundle
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
        testApplicationId = "com.raxdenstudios.mmdb.test"

        // apk name, is posible to add variables as version, date...
        project.setProperty("archivesBaseName", "mmdb")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.androidx.compose)
    implementationBundle(libs.bundles.coroutines)
    implementationBundle(libs.bundles.threetenabp)
    implementationBundle(libs.bundles.hilt)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.coil.compose)

    // debug libraries
    debugImplementation(project(":catalog"))
    debugImplementation(libs.leakcanary)
    debugImplementationBundle(libs.bundles.debug.androidx.compose)

    // test libraries
    testImplementation(project(":core:test"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
    testImplementation(libs.bundles.test.threetenabp)

    // instrumental test libraries
    androidTestImplementation(project(":core:test"))
    androidTestImplementationBundle(libs.bundles.test.android)
    androidTestImplementationBundle(libs.bundles.test.android)
    androidTestImplementationBundle(libs.bundles.test.coroutines)
    androidTestImplementationBundle(libs.bundles.test.threetenabp)
    androidTestImplementationBundle(libs.bundles.test.espresso)
    androidTestImplementationBundle(libs.bundles.test.androidx.compose)
    androidTestImplementationBundle(libs.bundles.test.hilt)
    androidTestImplementationBundle(libs.bundles.test.mockwebserver)
}
