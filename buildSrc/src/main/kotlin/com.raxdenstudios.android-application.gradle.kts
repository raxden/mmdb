plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.android.application")
  id("com.raxdenstudios.android-quality")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.appdistribution")
  id("com.raxdenstudios.android-commons")
}

testlogger {
  setTheme("mocha")
}

versioning {
  versionFilePath = "$rootDir/version.properties"
}

android {

  compileSdkVersion(Versions.compileSdk)

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  defaultConfig {
    applicationId = ApplicationId.id

    minSdkVersion(Versions.minSdk)
    targetSdkVersion(Versions.targetSdk)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    viewBinding = true
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  packagingOptions {
    exclude("META-INF/AL2.0")
    exclude("META-INF/LGPL2.1")
    exclude("META-INF/*.kotlin_module")
  }
}
