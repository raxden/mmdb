plugins {
  id("com.android.application")
  id("com.raxdenstudios.android-detekt")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("project-report")
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

    setProperty("archivesBaseName", "mmdb") // apk name, is posible to add variables as version, date...
  }

  signingConfigs {
    getByName("debug") {
      keyAlias = "androiddebugkey"
      keyPassword = "android"
      storeFile = file("../config/debug.keystore")
      storePassword = "android"
    }
    create("release") {
      keyAlias = "mmdb"
      keyPassword = "bob1YTMqc5acHN9spcYI"
      storeFile = file("../config/release.jks")
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
