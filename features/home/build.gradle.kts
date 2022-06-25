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

    javaCompileOptions {
      annotationProcessorOptions {
        arguments += mapOf(
          "room.schemaLocation" to "$projectDir/schemas",
          "room.incremental" to "true",
          "room.expandProjection" to "true"
        )
      }
    }
  }

  sourceSets {
    // Adds exported schema location as test app assets.
    getByName("debug").assets.srcDirs(files("$projectDir/schemas"))
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
  implementation(RaxdenLibraries.paginationCo)
  implementation(RaxdenLibraries.glide)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(AndroidLibraries.navigationUI)
  implementation(AndroidLibraries.navigationFragment)
  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
  androidTestImplementation(TestAndroidLibraries.room)
}
