plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(RaxdenLibraries.retrofitCo)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  testImplementation(project(Modules.baseTest))
}
