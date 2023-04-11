plugins {
    id("com.raxdenstudios.android-library")
}

android {
    namespace = "com.raxdenstudios.app.core.model"
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.threetenabp)
}
