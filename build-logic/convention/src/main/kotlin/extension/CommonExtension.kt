package extension

import Application
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import java.util.Properties

fun BaseAppModuleExtension.androidConfig(
    project: Project
) {
    compileSdk = Application.compileSdk

    compileOptions {
        sourceCompatibility = Application.sourceCompatibility
        targetCompatibility = Application.targetCompatibility
    }

    signingConfigs {
        getByName("debug") {
            storeFile = project.file("${project.rootDir}/config/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            project.getSigningConfigProperties("release").run {
                storeFile = project.file("${project.rootDir}/${getProperty("storeFile")}")
                storePassword = getProperty("storePassword")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = Application.id
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk

        // apk name, is posible to add variables as version, date...
        project.setProperty("archivesBaseName", "mmdb")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

private fun Project.getSigningConfigProperties(buildType: String): Properties {
    val properties = Properties()
    val propertiesFile = file("$rootDir/config/signing_$buildType.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { properties.load(it) }
    } else {
        println("No signing config found for build type $buildType")
    }
    return properties
}

fun LibraryExtension.androidConfig() {
    compileSdk = Application.compileSdk

    compileOptions {
        sourceCompatibility = Application.sourceCompatibility
        targetCompatibility = Application.targetCompatibility
    }

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk // needed for instrumental tests
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

fun CommonExtension<*, *, *, *>.testsConfig() {
    defaultConfig {
        testInstrumentationRunner = Application.testInstrumentationRunner
    }
    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
}

fun CommonExtension<*, *, *, *>.composeConfig(
    libs: VersionCatalog
) {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions_composeCompiler
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

fun CommonExtension<*, *, *, *>.roomConfig(
    project: Project
) {
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug")
            .assets
            .srcDirs(project.files("${project.projectDir}/schemas"))
    }
}

fun BaseAppModuleExtension.proguardConfig() {
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "proguard-android-optimize.txt",
                "proguard-rules.pro"
            )
        }
    }
}

fun LibraryExtension.proguardConfig() {
    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}
