plugins {
  id("com.android.dynamic-feature")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("project-report")
}

android {

  compileSdk = Versions.compileSdk

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  defaultConfig {
    minSdk = Versions.minSdk
//    targetSdkVersion(Versions.targetSdk)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    consumerProguardFile("consumer-rules.pro")
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
    resources {
      excludes.add("META-INF/AL2.0")
      excludes.add("META-INF/LGPL2.1")
      excludes.add("META-INF/*.kotlin_module")
    }
  }
}
