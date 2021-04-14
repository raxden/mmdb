plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-application")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.appdistribution")
}

versioning {
  versionFilePath = "./config/version.properties"
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryThreeten))
  implementation(project(Modules.libraryFirebase))

  implementation(project(Modules.base))

  implementation(project(Modules.featureBase))
  implementation(project(Modules.featureNetwork))
  implementation(project(Modules.featureMovie))
  implementation(project(Modules.featureAccount))
  implementation(project(Modules.featureList))

  implementation(project(Modules.featureHome))
  implementation(project(Modules.featureError))
  implementation(project(Modules.featureLogin))
  implementation(project(Modules.featureTMDBConnect))

  implementation(project(Modules.navigator))

  debugImplementation(DebugLibraries.leakcanary)
  debugImplementation(DebugLibraries.ganderDebug)
  releaseImplementation(DebugLibraries.ganderRelease)
}
