plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(platform(KotlinLibraries.coroutinesBom))
  api(KotlinLibraries.coroutinesAndroid)
}
