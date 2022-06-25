plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
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

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
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
  implementation(RaxdenLibraries.paginationCo)
  implementation(RaxdenLibraries.okhttp3)
  implementation(RaxdenLibraries.retrofitCo)
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.gson)

  debugImplementation(DebugLibraries.ganderDebug)
  releaseImplementation(DebugLibraries.ganderRelease)

  testImplementation(project(Modules.baseTest))
}
