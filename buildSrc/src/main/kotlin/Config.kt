object Config {

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // Enables code shrinking for the release build type.
    val isDebugMinifyEnabled = false

    val excludeResources = listOf(
        "META-INF/AL2.0",
        "META-INF/LGPL2.1",
        "META-INF/*.kotlin_module",
        "META-INF/LICENSE.md",
        "META-INF/LICENSE-notice.md",
        "LICENSE-notice.md",
    )
}