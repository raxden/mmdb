import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-compose-library")
}

android {
    namespace = "com.raxdenstudios.app.catalog"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    implementationBundle(libs.bundles.material)
    implementationBundle(libs.bundles.firebase)
    implementationBundle(libs.bundles.androidx.compose)
}
