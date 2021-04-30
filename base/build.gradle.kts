plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(RaxdenLibraries.android)
  api(RaxdenLibraries.coroutines)

  api(Libraries.koinAndroid)
  api(Libraries.koinAndroidExtensions)
}
