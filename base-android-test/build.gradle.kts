plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

    packagingOptions {
        resources {
            excludes.addAll(Config.excludeResources)
        }
    }
}

dependencies {
    api(RaxdenLibraries.androidTest)
    api(RaxdenLibraries.coroutinesTest)
    api(RaxdenLibraries.threeten)

    api(TestLibraries.restMock)
    api(TestLibraries.koinTest)
    api(TestLibraries.koinTestJunit)
    api(TestLibraries.timberJunit)
}
