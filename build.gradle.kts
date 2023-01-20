import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    /**
     * ====== Plugins DSL ======
     * You should use `apply false` in the top-level build.gradle file
     * to add a Gradle plugin as a build dependency, but not apply it to the
     * current (root) project. You should not use `apply false` in sub-projects.
     * For more information, see
     * Applying external plugins with same version to subprojects.
     * More info -> https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block
     */
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.play.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.appdistribution) apply false
    alias(libs.plugins.junit.jacoco)
    alias(libs.plugins.android.releasing)
    alias(libs.plugins.android.versioning) apply false
    alias(libs.plugins.test.logger)
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.gradle.play.publisher) apply false
    alias(libs.plugins.benManes)
    alias(libs.plugins.versionCatalogUpdate)
}

junitJacoco {
    excludes = listOf(
        "**/di/*",
        "**/BuildConfig*",
        "**/databinding/*",
        "**/*_*.class",
        "**/*_Impl*.class"
    )
}

// Dependencies
// ./gradlew versionCatalogUpdate
// ./gradlew versionCatalogUpdate --interactive
// ./gradlew versionCatalogApplyUpdates
// More info -> https://github.com/littlerobots/version-catalog-update-plugin
versionCatalogUpdate {
    // sort the catalog by key (default is true
    sortByKey.set(true)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

// Task to discard the version of the dependencies that are not stable, used by versionCatalogUpdate plugin
// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

// Detekt info ->
// ./gradlew detekt               - Runs a detekt analysis and complexity report on your source files
// ./gradlew detektGenerateConfig - Generates a default detekt configuration file into your project directory.
// ./gradlew detektBaseline       - Similar to detekt, but creates a code smell baseline. Further detekt runs will
//                                      only feature new smells not in this list.
// More info -> https://detekt.dev/docs/gettingstarted/gradle/
detekt {
    // version found will be used. Override to stay on the same version.
    config = files("/config/detekt/detekt.yml")
    // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects.
    parallel = true
    // Android: Don't create tasks for the specified build types (e.g. "release")
    ignoredBuildTypes = listOf("release")
    // Specify the base path for file paths in the formatted reports.
    basePath = "${rootProject.projectDir}"
}

subprojects {
    apply(plugin = rootProject.libs.plugins.test.logger.get().pluginId)
    apply(plugin = rootProject.libs.plugins.detekt.plugin.get().pluginId)
    // https://docs.gradle.org/current/userguide/project_report_plugin.html#sec:project_reports_tasks
    apply(plugin = "project-report")

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
    }

    dependencies {
        // This rule set provides wrappers for rules implemented by ktlint - https://ktlint.github.io/.
        // https://detekt.dev/docs/rules/formatting/dawd
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting")
    }
}

// https://stackoverflow.com/questions/50508926/why-delete-rootproject-builddir-task-in-gradle-removed-the-build-directory-in-th
tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
