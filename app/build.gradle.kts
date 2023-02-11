import java.util.Properties

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
            getSigningConfigProperties("debug").run {
                storeFile = file("$rootDir${getProperty("storeFile")}")
                storePassword = getProperty("storePassword")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPassword")
            }
        }
        create("release") {
            getSigningConfigProperties("release").run {
                storeFile = file("$rootDir${getProperty("storeFile")}")
                storePassword = getProperty("storePassword")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            addManifestPlaceholders(mapOf("crashlyticsCollectionEnabled" to false))
            applicationIdSuffix = ".debug"
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            firebaseAppDistribution {
                appId = "1:657739180104:android:7000f519ef3acb0bea507d"
                releaseNotesFile = "$rootDir/release_notes.txt"
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

fun getSigningConfigProperties(buildType: String): Properties {
    val properties = Properties()
    val propertiesFile = file("$rootDir/config/signing_$buildType.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
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
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
