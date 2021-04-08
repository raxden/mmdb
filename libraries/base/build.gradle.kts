plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(KotlinLibraries.kotlinStdlib)
  api(KotlinLibraries.kotlinReflect)

  api(Libraries.timber)

  testImplementation(TestLibraries.atslJunit)
}
