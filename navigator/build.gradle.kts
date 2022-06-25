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

  buildFeatures {
    viewBinding = true
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
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))

  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureSplash))
  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureMediaList))
  implementation(project(Modules.featureLogin))
}
