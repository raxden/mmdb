plugins {
  id("com.raxdenstudios.android-library")
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

  implementation(Libraries.gson)

  testImplementation(project(Modules.baseTest))
}
