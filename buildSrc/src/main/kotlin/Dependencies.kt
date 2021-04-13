object AndroidLibraries {
  const val material = "com.google.android.material:material:${Versions.material}"
  const val playCore = "com.google.android.play:core:${Versions.playCore}"
  const val constraintLayout =
    "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
  const val swipeRefreshLayout =
    "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
  const val browser = "androidx.browser:browser:${Versions.browser}"
  const val kotlinCore = "androidx.core:core-ktx:${Versions.kotlinCore}"
  const val kotlinActivity = "androidx.activity:activity-ktx:${Versions.kotlinActivity}"
  const val kotlinFragment = "androidx.fragment:fragment-ktx:${Versions.kotlinFragment}"
  const val kotlinPreferences = "androidx.preference:preference-ktx:${Versions.kotlinPreferences}"

  // Lifecycle
  const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
  const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
  const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
  const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
}

object KotlinLibraries {
  // https://medium.com/@mbonnin/the-different-kotlin-stdlibs-explained-83d7c6bf293
  const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
  const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

  const val coroutinesBom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.coroutines}"
  const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
}

object FirebaseLibraries {
  const val firebaseBoom = "com.google.firebase:firebase-bom:${Versions.firebase}"
  const val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
  const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
  const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
}

object Libraries {
  // Koin
  const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
  const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

  // Room
  const val roomRunTime = "androidx.room:room-runtime:${Versions.room}"
  const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
  const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

  // Retrofit
  const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val retrofitNetworkResponseAdapter = "com.github.haroldadmin:NetworkResponseAdapter:${Versions.retrofitNetworkResponseAdapter}"

  // OkHttp
  const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"
  const val okHttp = "com.squareup.okhttp3:okhttp"
  const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

  // Gson
  const val gson = "com.google.code.gson:gson:${Versions.gson}"

  // Glide
  const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
  const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

  // Timber
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

  // Threetenabp
  const val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"
}

object DebugLibraries {
  const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"

  // gander
  const val ganderDebug = "com.ashokvarma.android:gander-imdb:${Versions.gander}"
  const val ganderRelease = "com.ashokvarma.android:gander-no-op:${Versions.gander}"
}

object TestLibraries {
  const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test"

  // ANDROID TEST
  const val archCoreTest = "androidx.arch.core:core-testing:${Versions.archCoreTest}"
  const val atslJunit = "androidx.test.ext:junit-ktx:${Versions.atslJunit}"
  const val atslRunner = "androidx.test:runner:${Versions.atsl}"
  const val atslRules = "androidx.test:rules:${Versions.atsl}"

  // Robolectric
  const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

  // MOCKK
  const val mockkCore = "io.mockk:mockk:${Versions.mockk}"
  const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"

  // Koin
  const val koinTest = "org.koin:koin-test:${Versions.koin}"

  // MOCK WEBSERVER
  const val restMock = "com.github.andrzejchm.RESTMock:android:${Versions.restMock}"

  // Threetenabp
  const val threetenabp = "org.threeten:threetenbp:1.3.3"

  const val timberJunit = "net.lachlanmckee:timber-junit-rule:${Versions.timberJunit}"

}

object TestAndroidLibraries {
  const val fragmentTest = "androidx.fragment:fragment-testing:${Versions.fragmentTest}"

  const val room = "androidx.room:room-testing:${Versions.room}"

  const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
  const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
  const val espressoWebView = "androidx.test.espresso:espresso-web:${Versions.espresso}"
  const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
}
