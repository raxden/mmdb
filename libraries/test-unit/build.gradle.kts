plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)

  api(TestLibraries.koinTest)

  api(TestLibraries.threetenabp)

  api(TestLibraries.timberJunit)

  api(TestLibraries.robolectric)
}
