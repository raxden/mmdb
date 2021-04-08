plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryAndroidTest))
  implementation(project(Modules.libraryThreeten))
}
