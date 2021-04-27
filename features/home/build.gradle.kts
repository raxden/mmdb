plugins {
  id("com.raxdenstudios.android-feature")
}

android {

  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }

  defaultConfig {
    javaCompileOptions {
      annotationProcessorOptions {
        arguments(mapOf("room.schemaLocation" to "$projectDir/schemas"))
      }
    }
  }
  sourceSets {
    // Adds exported schema location as test app assets.
    getByName("debug").assets.srcDirs(files("$projectDir/schemas"))
  }
}

dependencies {
  implementation(RaxdenLibraries.threeten)
  implementation(RaxdenLibraries.paginationCo)
  implementation(RaxdenLibraries.glide)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
  androidTestImplementation(TestAndroidLibraries.room)
}
