plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("project-report")
}

android {

    compileSdk = Versions.compileSdk

    compileOptions {
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = Config.testInstrumentationRunner
        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = Config.isDebugMinifyEnabled
        }
    }

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }

    packagingOptions {
        resources {
            excludes.addAll(Config.excludeResources)
        }
    }
}

dependencies {
    implementation(RaxdenLibraries.paginationCo)
    implementation(RaxdenLibraries.okhttp3)
    implementation(RaxdenLibraries.retrofitCo)
    implementation(RaxdenLibraries.threeten)

    implementation(project(Modules.base))

    implementation(RaxdenLibraries.android)
    implementation(RaxdenLibraries.coroutines)
    implementation(Libraries.timber)
    implementation(Libraries.gson)

    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)

    debugImplementation(DebugLibraries.ganderDebug)
    releaseImplementation(DebugLibraries.ganderRelease)

    testImplementation(project(Modules.baseTest))
}
