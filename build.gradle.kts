buildscript {
  val kotlin_version by extra("1.4.32")
  repositories {
    google()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.google.gms:google-services:4.3.5")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.1")
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.1.0")
    classpath("com.raxdenstudios:android-plugins:0.41")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
  }
}

plugins {
  id("com.vanniktech.android.junit.jacoco").version("0.16.0")
}

junitJacoco {

}

allprojects {
  repositories {
    google()
    jcenter()
    mavenLocal()
    maven("https://jitpack.io")
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
