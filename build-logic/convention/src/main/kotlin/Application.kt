import org.gradle.api.JavaVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion

object Application {

    const val id = "com.raxdenstudios.mmdb"
    const val testInstrumentationRunner = "com.raxdenstudios.app.test.AppTestRunner"
    const val minSdk = 21
    const val compileSdk = 33
    const val targetSdk = 33

    private const val version = 11
    val javaLanguageVersion = JavaLanguageVersion.of(version)
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11
}
