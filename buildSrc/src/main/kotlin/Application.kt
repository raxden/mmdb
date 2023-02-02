import org.gradle.api.JavaVersion

object Application {

    const val id = "com.raxdenstudios.mmdb"
    const val testInstrumentationRunner = "com.raxdenstudios.app.test.AppTestRunner"
    const val minSdk = 21
    const val compileSdk = 33
    const val targetSdk = 33
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
}
