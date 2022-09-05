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
  api(RaxdenLibraries.coroutinesTest)

  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)
  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)
  api(TestLibraries.koinTest)
  api(TestLibraries.koinTestJunit)
  api(TestLibraries.timberJunit)
  api(TestLibraries.threetenabp)
  api(TestLibraries.turbine)
  api(TestLibraries.truth)
}
