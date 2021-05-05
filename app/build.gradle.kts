plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-application")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.appdistribution")
  id("com.github.triplet.play")
}

versioning {
  versionFilePath = "$rootDir/version.properties"
}

play {
  track.set("beta")
}

android {

  buildTypes {
    getByName("release") {
      firebaseAppDistribution {
        releaseNotesFile = "$rootDir/release_notes.txt"
        groups = "mmdb-team"
      }
    }
  }
}

dependencies {
  implementation(RaxdenLibraries.threeten)

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureSplash))
  implementation(project(Modules.featureMedia))
  implementation(project(Modules.featureAccount))
  implementation(project(Modules.featureMediaList))
  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureError))
  implementation(project(Modules.featureLogin))
  implementation(project(Modules.featureTMDBConnect))
  implementation(project(Modules.libraryNetwork))
  implementation(project(Modules.navigator))

  implementation(platform(FirebaseLibraries.firebaseBoom))
  implementation(FirebaseLibraries.firebaseCrashlytics)

  debugImplementation(DebugLibraries.leakcanary)

  debugImplementation(DebugLibraries.ganderDebug)
  releaseImplementation(DebugLibraries.ganderRelease)
}
