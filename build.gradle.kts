import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

buildscript {
  repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.android.tools.build:gradle:${Versions.androidGradlePlugin}")
    classpath("com.raxdenstudios:android-plugins:${Versions.androidPlugins}")
    classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroidGradlePlugin}")
    classpath("com.google.gms:google-services:${Versions.playServices}")
    classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}")
    classpath("com.google.firebase:firebase-appdistribution-gradle:${Versions.firebaseAppDistribution}")
  }
}

plugins {
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

subprojects {
  apply(plugin = "com.adarshr.test-logger")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  testlogger {
    theme = ThemeType.MOCHA
    slowThreshold = 3000
  }

  configure<DetektExtension> {
    // To create detekt.yml -> gradle detektGenerateConfig
    toolVersion = Versions.detektPlugin
    config = files("${project.rootDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    reports.html.enabled = true
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
