plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(Libraries.threetenabp)

  testImplementation(project(Modules.libraryTestCo))
}
