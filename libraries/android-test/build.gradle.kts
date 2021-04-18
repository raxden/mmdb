plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.restMock)

  api(TestAndroidLibraries.espresso)
  api(TestAndroidLibraries.espressoContrib)
  api(TestAndroidLibraries.espressoIntents)
}
