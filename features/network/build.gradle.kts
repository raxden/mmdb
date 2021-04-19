plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(RaxdenLibraries.okhttp3)
  implementation(RaxdenLibraries.retrofitCo)
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
