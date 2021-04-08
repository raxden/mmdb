plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(project(Modules.libraryTest))

  api(platform(KotlinLibraries.coroutinesBom))
  api(TestLibraries.coroutinesTest)
}
