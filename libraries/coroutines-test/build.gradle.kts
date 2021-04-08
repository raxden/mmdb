plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(TestLibraries.atslJunit)

  api(platform(KotlinLibraries.coroutinesBom))
  api(TestLibraries.coroutinesTest)
}
