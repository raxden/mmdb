plugins {
  `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
  mavenCentral()
  google()
  jcenter()
  maven("https://plugins.gradle.org/m2/")
}

dependencies {
  implementation("com.android.tools.build:gradle:4.1.1")
  implementation("org.ajoberstar.grgit:grgit-gradle:4.1.0")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
  implementation(kotlin("gradle-plugin", version = "1.4.32"))
  implementation(gradleApi())
  implementation(localGroovy())
}
