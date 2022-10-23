import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.raxdenstudios:android-plugins:${Versions.androidPlugins}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroidGradlePlugin}")
        classpath("com.google.gms:google-services:${Versions.playServices}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}")
        classpath("com.google.firebase:firebase-appdistribution-gradle:${Versions.firebaseAppDistribution}")
    }
}

plugins {
    /**
     * You should use `apply false` in the top-level build.gradle file
     * to add a Gradle plugin as a build dependency, but not apply it to the
     * current (root) project. You should not use `apply false` in sub-projects.
     * For more information, see
     * Applying external plugins with same version to subprojects.
     */
    id("com.android.application") version Versions.androidGradlePlugin apply false
    id("com.android.library") version Versions.androidGradlePlugin apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false

    id("com.vanniktech.android.junit.jacoco") version Versions.jacocoPlugin
    id("com.raxdenstudios.android-releasing") version Versions.androidPlugins
    id("com.raxdenstudios.android-versioning") version Versions.androidPlugins apply false
    id("com.adarshr.test-logger") version Versions.testLoggerPlugin
    id("io.gitlab.arturbosch.detekt") version Versions.detektPlugin
    id("com.github.ben-manes.versions") version Versions.benNamesPlugin
    id("com.github.triplet.play") version Versions.tripletPlugin apply false
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    outputFormatter = "html"
}

junitJacoco {
    excludes = listOf(
        "**/di/*",
        "**/BuildConfig*",
        "**/databinding/*",
        "**/*_*.class",
        "**/*_Impl*.class"
    )
}

// Detekt info -> https://detekt.dev/docs/gettingstarted/gradle/
detekt {
    // version found will be used. Override to stay on the same version.
    toolVersion = Versions.detektPlugin
    config = files("/config/detekt/detekt.yml")
    // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects.
    parallel = true
    // Android: Don't create tasks for the specified build types (e.g. "release")
    ignoredBuildTypes = listOf("release")
    // Specify the base path for file paths in the formatted reports.
    basePath = "${rootProject.projectDir}"
}

subprojects {
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
