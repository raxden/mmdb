import gradle.kotlin.dsl.accessors._a20556e8777b0df05cf484c9f759b14a.testlogger

plugins {
  id("com.android.dynamic-feature")
  id("com.raxdenstudios.android-quality")
  id("com.raxdenstudios.android-commons")
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
    minSdkVersion(Versions.minSdk)
    targetSdkVersion(Versions.targetSdk)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFile("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }
  }

//  buildFeatures {
//    viewBinding = true
//  }
  viewBinding.isEnabled = true

  kotlinOptions {
    jvmTarget = "1.8"
  }

  packagingOptions {
    exclude("META-INF/AL2.0")
    exclude("META-INF/LGPL2.1")
    exclude("META-INF/*.kotlin_module")
  }
}
