pluginManagement {
    /**
     * The pluginManagement {repositories {...}} block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use
     * local repositories or define your own remote repositories. The code below
     * defines the Gradle Plugin Portal, Google's Maven repository,
     * and the Maven Central Repository as the repositories Gradle should use to look for its dependencies.
     */
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    /**
     * The dependencyResolutionManagement { repositories {...}}
     * block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle file. For new projects,
     * Android Studio includes Google's Maven repository and the
     * Maven Central Repository by
     * default, but it does not configure any dependencies (unless you select a
     * template that requires some).
     */
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.11.2"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        // Publishing a build scan for every build execution
        publishAlways()
    }
}

include(":app")

include(":feature:home")
include(":feature:list")
include(":feature:account")
include(":feature:search")
include(":feature:detail")

include(":core:ui")
include(":core:network")
include(":core:domain")
include(":core:data")
include(":core:common")
include(":core:navigation")
include(":core:model")
include(":core:test")
include(":core:database")
