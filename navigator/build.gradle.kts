plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryThreeten))

  implementation(project(Modules.base))

  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureMovieList))
  implementation(project(Modules.featureLogin))
}
