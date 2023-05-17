package extension

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal val VersionCatalog.versions_composeCompiler: String
    get() = getVersion("compose-compiler")

internal val VersionCatalog.versions_minSDK: Int
    get() = getVersion("min-sdk").toInt()

internal val VersionCatalog.versions_compileSDK: Int
    get() = getVersion("compile-sdk").toInt()

internal val VersionCatalog.versions_targetSDK: Int
    get() = getVersion("target-sdk").toInt()

internal val VersionCatalog.versions_sourceCompatibility: JavaVersion
    get() = getVersion("source-compatibility").toJavaVersion()

internal val VersionCatalog.versions_targetCompatibility: JavaVersion
    get() = getVersion("target-compatibility").toJavaVersion()

internal val VersionCatalog.version_jdk: JavaLanguageVersion
    get() = JavaLanguageVersion.of(getVersion("jdk"))

private fun VersionCatalog.getVersion(version: String) =
    findVersion(version).get().toString()

@Suppress("CyclomaticComplexMethod", "UnstableApiUsage")
private fun String.toJavaVersion() : JavaVersion = when (this) {
    "1.6" -> JavaVersion.VERSION_1_6
    "1.7" -> JavaVersion.VERSION_1_7
    "1.8" -> JavaVersion.VERSION_1_8
    "9" -> JavaVersion.VERSION_1_9
    "10" -> JavaVersion.VERSION_1_10
    "11" -> JavaVersion.VERSION_11
    "12" -> JavaVersion.VERSION_12
    "13" -> JavaVersion.VERSION_13
    "14" -> JavaVersion.VERSION_14
    "15" -> JavaVersion.VERSION_15
    "16" -> JavaVersion.VERSION_16
    "17" -> JavaVersion.VERSION_17
    "18" -> JavaVersion.VERSION_18
    "19" -> JavaVersion.VERSION_19
    "20" -> JavaVersion.VERSION_20
    "21" -> JavaVersion.VERSION_21
    "22" -> JavaVersion.VERSION_22
    "23" -> JavaVersion.VERSION_23
    "24" -> JavaVersion.VERSION_24
    else -> JavaVersion.VERSION_1_8
}
