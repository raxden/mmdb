plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(RaxdenLibraries.retrofitCo)
  implementation(RaxdenLibraries.threeten)
  implementation(RaxdenLibraries.paginationCo)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))
  implementation(project(Modules.libraryNetwork))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
