plugins {
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")

  // The Project report plugin adds some tasks to your project which generate reports containing
  // useful information about your build.
  // More info -> https://docs.gradle.org/current/userguide/project_report_plugin.html
  id("project-report")

  // A Gradle plugin for printing beautiful logs on the console while running tests
  // More info -> https://github.com/radarsh/gradle-test-logger-plugin
  id("com.adarshr.test-logger")
}
