import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  id("io.gitlab.arturbosch.detekt")
}

configure<DetektExtension> {
  // To create detekt.yml -> gradle detektGenerateConfig
  toolVersion = "1.15.0"
  config = files("${project.rootDir}/config/detekt/detekt.yml")
  buildUponDefaultConfig = true

  reports {
    html {
      enabled = true
    }
  }
}
