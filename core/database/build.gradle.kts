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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    namespace = "com.raxdenstudios.app.core.database"

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug").assets.srcDirs(files("$projectDir/schemas"))
    }

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
    androidTestImplementation(libs.room.test)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testingUI)
    kaptAndroidTest(libs.hilt.compiler)
}
