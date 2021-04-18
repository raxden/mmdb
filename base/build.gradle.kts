plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryAndroid))
  api(project(Modules.libraryCoroutines))

  api(Libraries.koinScope)
  api(Libraries.koinViewModel)
}
