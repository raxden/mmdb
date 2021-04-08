plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryCoroutines))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryPaginationCo))
  implementation(project(Modules.libraryGlide))

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)
  implementation(Libraries.roomRX)

  testImplementation(project(Modules.baseTest))
}
