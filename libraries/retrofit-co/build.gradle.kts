plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryRetrofit))
  api(project(Modules.libraryCoroutines))

  api(Libraries.retrofitNetworkResponseAdapter)

  testImplementation(TestLibraries.atslJunit)
}
