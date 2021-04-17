plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-application")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.appdistribution")
}

android {

  defaultConfig {
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
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryFirebase))

  implementation(project(Modules.base))

  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureNetwork))
  implementation(project(Modules.featureMovie))
  implementation(project(Modules.featureAccount))
  implementation(project(Modules.featureMovieList))

  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureError))
  implementation(project(Modules.featureLogin))
  implementation(project(Modules.featureTMDBConnect))

  implementation(project(Modules.navigator))

  debugImplementation(DebugLibraries.leakcanary)
  debugImplementation(DebugLibraries.ganderDebug)
  releaseImplementation(DebugLibraries.ganderRelease)
}
