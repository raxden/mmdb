plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {

    compileSdk = Application.compileSdk

    compileOptions {
        sourceCompatibility = Application.sourceCompatibility
        targetCompatibility = Application.targetCompatibility
    }

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk

        testInstrumentationRunner = Application.testInstrumentationRunner
        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
        }
    }

    namespace = "com.raxdenstudios.app.feature.detail"

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlin.jvm.get()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

dependencies {
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.coreUI))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreDomain))
    implementation(libs.commons.android)
    implementation(libs.commons.paginationCo)
    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.landscapists)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // debug libraries
    debugImplementation(platform(libs.firebase.bom))
    debugImplementation(libs.bundles.compose.debug)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.app))
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(project(Modules.coreNetwork))
    androidTestImplementation(project(Modules.coreUI))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
