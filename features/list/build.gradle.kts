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
  implementation(RaxdenLibraries.retrofitCo)
  implementation(RaxdenLibraries.threeten)
  implementation(RaxdenLibraries.paginationCo)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
