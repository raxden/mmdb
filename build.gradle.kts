buildscript {
  repositories {
    google()
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.google.gms:google-services:4.3.5")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.1.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
  }
}

plugins {
  id("com.raxdenstudios.android-releasing").version("0.41")
  id("com.raxdenstudios.android-versioning").version("0.41") apply false
  id("com.vanniktech.android.junit.jacoco").version("0.16.0")
  id("com.adarshr.test-logger").version("3.0.0")
  id("io.gitlab.arturbosch.detekt").version("1.15.0")
  id("com.github.triplet.play").version("3.4.0") apply false
}

releasing {

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

allprojects {
  repositories {
    google()
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
  }
}

subprojects {
  apply(plugin = "com.adarshr.test-logger")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  testlogger {
    setTheme("mocha")
  }

  detekt {
    toolVersion = "1.15.0"
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
