plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))

  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureMediaList))
  implementation(project(Modules.featureLogin))
}
