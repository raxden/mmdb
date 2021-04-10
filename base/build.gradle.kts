plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryCoroutines))
}
