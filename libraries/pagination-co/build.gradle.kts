plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryPagination))
  api(project(Modules.libraryCoroutines))

  testImplementation(TestLibraries.atslJunit)
  testImplementation(TestLibraries.mockkCore)
  testImplementation(TestLibraries.mockkAndroid)
  testImplementation(project(Modules.libraryCoroutinesTest))
}
