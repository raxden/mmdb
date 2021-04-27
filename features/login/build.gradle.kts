plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(RaxdenLibraries.threeten)
  implementation(RaxdenLibraries.paginationCo)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  testImplementation(project(Modules.baseTest))
}
