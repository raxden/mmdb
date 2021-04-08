plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(platform(FirebaseLibraries.firebaseBoom))
  api(FirebaseLibraries.firebaseAnalytics)
  api(FirebaseLibraries.firebaseCrashlytics)
  api(FirebaseLibraries.firebaseMessaging)
}
