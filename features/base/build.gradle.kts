plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryPaginationCo))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryGlide))

  implementation(project(Modules.base))

  implementation(Libraries.gson)
}
