object RaxdenLibraries {

    const val android = "com.raxdenstudios:commons-android:${Versions.commonsVersion}"
    const val androidTest = "com.raxdenstudios:commons-android-test:${Versions.commonsVersion}"
    const val coroutines = "com.raxdenstudios:commons-coroutines:${Versions.commonsVersion}"
    const val coroutinesTest = "com.raxdenstudios:commons-coroutines-test:${Versions.commonsVersion}"
    const val glide = "com.raxdenstudios:commons-glide:${Versions.commonsVersion}"
    const val okhttp3 = "com.raxdenstudios:commons-okhttp3:${Versions.commonsVersion}"
    const val paginationCo = "com.raxdenstudios:commons-pagination-co:${Versions.commonsVersion}"
    const val permissions = "com.raxdenstudios:commons-permissions:${Versions.commonsVersion}"
    const val retrofitCo = "com.raxdenstudios:commons-retrofit-co:${Versions.commonsVersion}"
    const val threeten = "com.raxdenstudios:commons-threeten:${Versions.commonsVersion}"
}

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
    const val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object AndroidLifecycleLibraries {

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
}

object ComposeLibraries {

    // https://developer.android.com/jetpack/compose/setup#bom-version-mapping
    const val bom = "androidx.compose:compose-bom:2022.12.00"

    // Choose one of the following:
    // Material Design 3
    const val material3 = "androidx.compose.material3:material3"

    // or Material Design 2
    const val material = "androidx.compose.material:material"

    // or skip Material Design and build directly on top of foundational components
    const val foundation = "androidx.compose.foundation:foundation"

    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    const val ui = "androidx.compose.ui:ui"

    // Android Studio Preview support
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"

    // UI Tests
    const val uiTestjunit4 = "androidx.compose.ui:ui-test-junit4"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    const val materialIconsCore = "androidx.compose.material:material-icons-core"

    // Optional - Add full set of material icons
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended"

    // Optional - Add window size utils
    const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class"

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayoutCompose}"

    // Optional - Integration with activities
    const val activityCompose = "androidx.activity:activity-compose:1.5.1"

    // Optional - Integration with hilt
    const val hiltCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"

    // Optional - Integration with ViewModels
    const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"

    // Optional - Integration with LiveData
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata"

    // Optional - Integration with RxJava
    const val rxJava = "androidx.compose.runtime:runtime-rxjava2"
}

object AndroidComposeAccompanistLibraries {

    // Accompanist - Utils for Jetpack Compose - https://github.com/google/accompanist
    // Reuse your View theme in Compose - When using a MDC theme
    const val composeMaterialThemeAdapter =
        "com.google.accompanist:accompanist-themeadapter-material:${Versions.accompanist}"

    // Reuse your View theme in Compose - When using a AppCompat theme
    const val composeAppCompatThemeAdapter =
        "com.google.accompanist:accompanist-appcompat-theme:${Versions.accompanist}"

    // https://google.github.io/accompanist/pager/
    const val pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    // https://google.github.io/accompanist/systemuicontroller/
    const val systemuicontroller =
        "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
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

object HiltLibraries {

    const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val androidTesting = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
}

object RoomLibraries {

    const val roomRunTime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomTesting = "androidx.room:room-testing:${Versions.room}"
}

object RetrofitLibraries {

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitNetworkResponseAdapter =
        "com.github.haroldadmin:NetworkResponseAdapter:${Versions.retrofitNetworkResponseAdapter}"
}

object LandscapistLibraries {

    // https://github.com/skydoves/landscapist
    const val bom = "com.github.skydoves:landscapist-bom:${Versions.landscapist}"
    const val coil = "com.github.skydoves:landscapist-coil"
    const val glide = "com.github.skydoves:landscapist-glide"
    const val animation = "com.github.skydoves:landscapist-animation"
    const val placeholder = "com.github.skydoves:landscapist-placeholder"
    const val palette = "com.github.skydoves:landscapist-palette"
}

object CoilLibraries {

    const val bom = "io.coil-kt:coil-bom:${Versions.coil}"
    const val core = "io.coil-kt:coil:${Versions.coil}"
    const val compose = "io.coil-kt:coil-compose:${Versions.coil}"
}

object OKHttpLibraries {

    const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"
}

object Libraries {

    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
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

    // MOCK WEBSERVER
    const val restMock = "com.github.andrzejchm.RESTMock:android:${Versions.restMock}"

    // Threetenabp
    const val threetenabp = "org.threeten:threetenbp:1.3.3"

    const val timberJunit = "net.lachlanmckee:timber-junit-rule:${Versions.timberJunit}"

    const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    const val truth = "com.google.truth:truth:${Versions.truth}"

}

object TestAndroidLibraries {

    const val fragmentTest = "androidx.fragment:fragment-testing:${Versions.fragmentTest}"

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espressoWebView = "androidx.test.espresso:espresso-web:${Versions.espresso}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
}
