plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(Libraries.retrofit)
  api(Libraries.retrofitGsonConverter)

  api(Libraries.gson)

  testImplementation(TestLibraries.atslJunit)
}
