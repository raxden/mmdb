plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.application)
    alias(libs.plugins.play.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.gradle.play.publisher)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.parcelize)
}

versioning {
    versionFilePath = "$rootDir/version.properties"
}

play {
    track.set("beta")
}

android {

    compileSdk = Application.compileSdk

    compileOptions {
        sourceCompatibility = Application.sourceCompatibility
        targetCompatibility = Application.targetCompatibility
    }

    defaultConfig {
        applicationId = Application.id

        minSdk = Application.minSdk
        targetSdk = Application.targetSdk

        testInstrumentationRunner = Application.testInstrumentationRunner

        // apk name, is posible to add variables as version, date...
        setProperty("archivesBaseName", "mmdb")
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("$rootDir/config/debug.keystore")
            storePassword = "android"
        }
        create("release") {
            keyAlias = "mmdb"
            keyPassword = "bob1YTMqc5acHN9spcYI"
            storeFile = file("$rootDir/config/release.jks")
            storePassword = "bob1YTMqc5acHN9spcYI"
        }
    }

    buildTypes {
        getByName("debug") {
            addManifestPlaceholders(mapOf("crashlyticsCollectionEnabled" to false))
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            addManifestPlaceholders(mapOf("crashlyticsCollectionEnabled" to true))
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                "proguard-android-optimize.txt",
                "proguard-rules.pro"
            )

            firebaseAppDistribution {
                releaseNotesFile = "$rootDir/release_notes.txt"
                groups = "mmdb-team"
            }
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlin.jvm.get()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

dependencies {
    implementation(project(Modules.coreCommon))
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
    implementation(project(Modules.featureHome))
    implementation(project(Modules.featureError))
    implementation(project(Modules.featureLogin))
    implementation(project(Modules.featureTMDBConnect))
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
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
