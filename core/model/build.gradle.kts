import extension.implementationBundle

plugins {
    id("com.raxdenstudios.android-library")
}

android {
    namespace = "com.raxdenstudios.app.core.model"
}

dependencies {
    implementationBundle(libs.bundles.threetenabp)
}
