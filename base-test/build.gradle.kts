plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(RaxdenLibraries.coroutinesTest)

  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)

  api(TestLibraries.koinTest)
  api(TestLibraries.timberJunit)
  api(TestLibraries.threetenabp)
}
