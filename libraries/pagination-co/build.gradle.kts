plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryPagination))
  api(project(Modules.libraryCoroutines))

  testImplementation(project(Modules.libraryTest))
  testImplementation(project(Modules.libraryCoroutinesTest))
}
