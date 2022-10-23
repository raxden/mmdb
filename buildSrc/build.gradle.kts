plugins {
    `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}
