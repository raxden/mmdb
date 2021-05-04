plugins {
  `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
  google()
  jcenter()
  mavenCentral()
  mavenLocal()
  maven("https://plugins.gradle.org/m2/")
}

dependencies {
  implementation("com.android.tools.build:gradle:4.1.1")
  implementation(kotlin("gradle-plugin", version = "1.4.32"))

  implementation("com.google.gms:google-services:4.3.5")
  implementation("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
  implementation("com.google.firebase:firebase-appdistribution-gradle:2.1.1")

  implementation("org.ajoberstar.grgit:grgit-gradle:4.1.0")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
  implementation("com.adarshr:gradle-test-logger-plugin:3.0.0")
  implementation("com.vanniktech:gradle-android-junit-jacoco-plugin:0.16.0")
  implementation("com.raxdenstudios:android-plugins:0.41")

  implementation(gradleApi())
  implementation(localGroovy())
}
