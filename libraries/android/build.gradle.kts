plugins {
  id("com.raxdenstudios.android-library")
}

android {
  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  api(project(Modules.libraryBase))

  api(AndroidLibraries.kotlinCore)
  api(AndroidLibraries.kotlinActivity)
  api(AndroidLibraries.kotlinFragment)
  api(AndroidLibraries.kotlinPreferences)
  api(AndroidLibraries.material)
  api(AndroidLibraries.playCore)
  api(AndroidLibraries.constraintLayout)
  api(AndroidLibraries.swipeRefreshLayout)
  api(AndroidLibraries.browser)
  api(AndroidLibraries.lifecycleExtensions)
  api(AndroidLibraries.lifecycleRuntime)
  api(AndroidLibraries.lifecycleCommon)
  api(AndroidLibraries.lifecycleViewModel)

  testImplementation(TestLibraries.atslJunit)
}
