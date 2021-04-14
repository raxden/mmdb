plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryPaginationCo))
  implementation(project(Modules.libraryThreeten))

  implementation(project(Modules.base))

  implementation(Libraries.gson)
}
