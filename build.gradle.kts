buildscript {
  val kotlin_version by extra("1.4.32")
  repositories {
    google()
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.android.tools.build:gradle:4.1.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

    classpath("com.google.gms:google-services:4.3.5")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.1.1")
  }
}

plugins {
  id("com.vanniktech.android.junit.jacoco")
  id("com.raxdenstudios.android-releasing")
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

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
