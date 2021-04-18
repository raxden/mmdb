plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryAndroid))

  testImplementation(TestLibraries.atslJunit)
}
