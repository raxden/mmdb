plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(AndroidLibraries.kotlinPreferences)
  api(Libraries.gson)

  testImplementation(project(Modules.libraryTest))
}
