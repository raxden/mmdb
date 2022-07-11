plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("kotlin-parcelize")
  id("project-report")
}

android {

  compileSdk = Versions.compileSdk

  compileOptions {
    sourceCompatibility = Versions.sourceCompatibility
    targetCompatibility = Versions.targetCompatibility
  }

  defaultConfig {
    minSdk = Versions.minSdk
    targetSdk = Versions.targetSdk

    testInstrumentationRunner = Versions.testInstrumentationRunner
    consumerProguardFile("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }
  }

  buildFeatures {
    viewBinding = true
  }

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
  }

  // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
  kapt {
    correctErrorTypes = true
  }

  packagingOptions {
    resources {
      excludes.add("META-INF/AL2.0")
      excludes.add("META-INF/LGPL2.1")
      excludes.add("META-INF/*.kotlin_module")
    }
  }
}

dependencies {
  implementation(RaxdenLibraries.okhttp3)
  implementation(RaxdenLibraries.retrofitCo)

  implementation(project(Modules.base))
  implementation(project(Modules.libraryNetwork))

  implementation(RaxdenLibraries.android)
  implementation(RaxdenLibraries.coroutines)
  implementation(Libraries.timber)
  implementation(Libraries.hiltAndroid)
  kapt(Libraries.hiltCompiler)

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
}
