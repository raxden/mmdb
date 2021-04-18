plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryRetrofitCo))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryPaginationCo))

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.roomRunTime)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomKtx)

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
