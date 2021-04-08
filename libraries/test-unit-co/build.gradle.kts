plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryTest))

  api(platform(KotlinLibraries.coroutinesBom))
  api(TestLibraries.coroutinesTest)
}
