plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(RaxdenLibraries.paginationCo)
  implementation(RaxdenLibraries.okhttp3)
  implementation(RaxdenLibraries.retrofitCo)
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.gson)

  debugImplementation(DebugLibraries.ganderDebug)
  releaseImplementation(DebugLibraries.ganderRelease)

  testImplementation(project(Modules.baseTest))
}
