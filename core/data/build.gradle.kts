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

        testInstrumentationRunner = Application.testInstrumentationRunner
        consumerProguardFile("consumer-rules.pro")
    }

    namespace = "com.raxdenstudios.app.core.data"

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlin.jvm.get()
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
    // libraries
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreNetwork))
    implementation(project(Modules.coreDatabase))
    implementation(libs.bundles.coroutines)
    implementation(libs.commons.android)
    implementation(libs.commons.paginationCo)
    implementation(libs.timber)
    implementation(libs.bundles.threetenabp)
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // test libraries
    testImplementation(project(Modules.coreTest))
    testImplementation(libs.bundles.testing)

    // instrumental test libraries
    androidTestImplementation(project(Modules.coreTest))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
