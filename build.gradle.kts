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
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.1.0")
    classpath("com.raxdenstudios:android-plugins:0.41")
  }
}

plugins {
  id("com.vanniktech.android.junit.jacoco").version("0.16.0")
  id("com.raxdenstudios.android-releasing").version("0.41")
}

junitJacoco {

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

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
