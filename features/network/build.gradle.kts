plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryOkHttp3))
  implementation(project(Modules.libraryRetrofitCo))
  implementation(project(Modules.libraryThreeten))

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
