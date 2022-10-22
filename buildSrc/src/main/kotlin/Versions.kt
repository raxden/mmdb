import org.gradle.api.JavaVersion

object Versions {
    const val minSdk = 21
    const val compileSdk = 32
    const val targetSdk = 32

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
    const val jvmTarget = "1.8"

    const val androidPlugins = "0.46"
    const val androidGradlePlugin = "7.2.2"
    const val hiltAndroidGradlePlugin = "2.42"
    const val jacocoPlugin = "0.16.0"
    const val nexusStagingPlugin = "0.30.0"
    const val testLoggerPlugin = "3.2.0"
    const val detektPlugin = "1.22.0-RC2"
    const val benNamesPlugin = "0.42.0"
    const val tripletPlugin = "3.7.0"
    const val playServices = "4.3.12"
    const val firebaseCrashlytics = "2.9.1"
    const val firebaseAppDistribution = "3.0.2"

    const val commonsVersion = "4.18.0"

    const val playCore = "1.10.3"
    const val material = "1.6.1"
    const val constraintLayout = "2.1.4"
    const val swipeRefreshLayout = "1.1.0"
    const val kotlinCore = "1.8.0"
    const val kotlinActivity = "1.5.0"
    const val kotlinFragment = "1.5.0"
    const val kotlinPreferences = "1.2.0"
    const val lifecycle = "2.5.0"
    const val browser = "1.4.0"
    const val navigation = "2.3.5"
    const val kotlin = "1.7.10"
    const val firebase = "26.2.0"

    const val coroutines = "1.6.3"
    const val rxAndroid = "2.1.1"
    const val rxKotlin = "2.4.0"

    const val koin = "3.2.0"
    const val hilt = hiltAndroidGradlePlugin

    const val room = "2.5.0-alpha02"

    const val gson = "2.9.0"
    const val okHttp = "4.10.0"
    const val restMock = "0.4.4"
    const val retrofit = "2.9.0"
    const val retrofitNetworkResponseAdapter = "5.0.0"

    const val leakcanary = "2.9.1"
    const val gander = "3.1.0"

    const val fragmentTest = "1.1.0"
    const val espresso = "3.4.0"
    const val mockk = "1.12.4"
    const val atsl = "1.4.0"
    const val atslJunit = "1.1.3"
    const val archCoreTest = "2.1.0"
    const val robolectric = "4.8.1"
    const val threetenabp = "1.4.0"
    const val glide = "4.13.2"
    const val timber = "4.7.1"
    const val timberJunit = "1.0.1"
    const val turbine = "0.9.0"
    const val truth = "1.1.3"
}
