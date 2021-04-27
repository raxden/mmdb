plugins {
  id("com.raxdenstudios.android-feature")
}

android {

  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }
}

dependencies {
  implementation(RaxdenLibraries.okhttp3)
  implementation(RaxdenLibraries.retrofitCo)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))
  implementation(project(Modules.libraryNetwork))

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
}
