
import extension.androidConfig
import extension.composeConfig
import extension.libs
import extension.proguardConfig
import extension.testsConfig
import extension.version_jdk

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    androidConfig(project)
    proguardConfig()
    testsConfig()
    composeConfig(project)

    kotlin {
        jvmToolchain(project.libs.version_jdk.asInt())
    }

    kotlinOptions {
        jvmTarget = project.libs.version_jdk.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }
}
