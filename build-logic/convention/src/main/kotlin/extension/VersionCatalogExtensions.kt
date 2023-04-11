package extension

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal val VersionCatalog.versions_composeCompiler: String
    get() = getVersion("compose-compiler")

private fun VersionCatalog.getVersion(version: String) =
    findVersion(version).get().toString()
