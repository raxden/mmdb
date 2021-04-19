import gradle.kotlin.dsl.accessors._c4b7c5df5154c3b61221505670b52303.testlogger

plugins {
  id("com.android.application")
  id("com.raxdenstudios.android-detekt")
  id("com.adarshr.test-logger")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("project-report")
}

testlogger {
  setTheme("mocha")
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
