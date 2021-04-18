plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryAndroidTest))
  api(project(Modules.libraryCoroutinesTest))
  api(project(Modules.libraryThreeten))

  api(TestLibraries.koinTest)
  api(TestLibraries.timberJunit)
}
