import extension.androidConfig
import extension.proguardConfig
import extension.testsConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    androidConfig()
    proguardConfig()
    testsConfig()

    kotlin {
        jvmToolchain(Application.javaLanguageVersion.asInt())
    }

    kotlinOptions {
        jvmTarget = Application.javaLanguageVersion.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }
}
