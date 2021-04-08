plugins {
  id("com.raxdenstudios.android-feature")
}

dependencies {
  implementation(project(Modules.libraryAndroid))
  implementation(project(Modules.libraryCoroutines))
  implementation(project(Modules.libraryKoin))
  implementation(project(Modules.libraryOkHttp3))
  implementation(project(Modules.libraryRetrofitCo))

  implementation(project(Modules.base))
  implementation(project(Modules.featureBase))

  testImplementation(project(Modules.baseTest))
  androidTestImplementation(project(Modules.baseAndroidTest))
}
