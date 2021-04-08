plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryAndroidTest))
  api(project(Modules.libraryCoroutinesTest))

  implementation(project(Modules.libraryThreeten))
}
