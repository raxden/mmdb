plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(RaxdenLibraries.androidTest)
  api(RaxdenLibraries.coroutinesTest)
  api(RaxdenLibraries.threeten)

  api(TestLibraries.koinTest)
  api(TestLibraries.timberJunit)
}
