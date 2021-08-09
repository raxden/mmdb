plugins {
  `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
  google()
  jcenter()
}

dependencies {
  implementation("com.android.tools.build:gradle:7.0.0")
  implementation(kotlin("gradle-plugin", version = "1.4.32"))

  implementation(gradleApi())
  implementation(localGroovy())
}
