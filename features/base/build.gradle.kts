plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(RaxdenLibraries.paginationCo)
  implementation(RaxdenLibraries.threeten)
  implementation(RaxdenLibraries.glide)

  implementation(project(Modules.base))

  implementation(Libraries.gson)
}
