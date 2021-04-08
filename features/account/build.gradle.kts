plugins {
  id("com.raxdenstudios.android-library")
}

android {
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
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryCoroutines))
  implementation(project(Modules.libraryRetrofitCo))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryPaginationCo))

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)
  implementation(Libraries.roomRX)

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
}
