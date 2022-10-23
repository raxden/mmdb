plugins {
    id("com.raxdenstudios.android-versioning")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("com.github.triplet.play")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("project-report")
}

versioning {
    versionFilePath = "$rootDir/version.properties"
}

play {
    track.set("beta")
}

android {

    compileSdk = Versions.compileSdk

    compileOptions {
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }

    defaultConfig {
        applicationId = ApplicationId.id

        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = Config.testInstrumentationRunner

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
            isMinifyEnabled = Config.isDebugMinifyEnabled
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            signingConfig = signingConfigs.getByName("debug")

            firebaseAppDistribution {
                appId = "1:657739180104:android:3cdc3635d3de8978ea507d"
                groups = "mmdb-team"
            }
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
                appId = "1:657739180104:android:3cdc3635d3de8978ea507d"
                groups = "mmdb-team"
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }

    kapt {
        // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
        correctErrorTypes = true
    }

    packagingOptions {
        resources {
            excludes.addAll(Config.excludeResources)
        }
    }
}

dependencies {
    implementation(project(Modules.base))
    implementation(project(Modules.featureSplash))
    implementation(project(Modules.featureMedia))
    implementation(project(Modules.featureAccount))
    implementation(project(Modules.featureMediaList))
    implementation(project(Modules.featureHome))
    implementation(project(Modules.featureError))
    implementation(project(Modules.featureLogin))
    implementation(project(Modules.featureTMDBConnect))
    implementation(project(Modules.libraryNetwork))
    implementation(project(Modules.navigator))

    implementation(RaxdenLibraries.android)
    implementation(RaxdenLibraries.coroutines)
    implementation(Libraries.timber)
    implementation(RaxdenLibraries.threeten)
    implementation(RaxdenLibraries.paginationCo)

    implementation(platform(FirebaseLibraries.firebaseBoom))
    implementation(FirebaseLibraries.firebaseCrashlytics)

    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)

    debugImplementation(DebugLibraries.leakcanary)
//  debugImplementation(DebugLibraries.ganderDebug)
//  releaseImplementation(DebugLibraries.ganderRelease)
}
