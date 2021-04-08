plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(Libraries.koinScope)
  api(Libraries.koinViewModel)
}
