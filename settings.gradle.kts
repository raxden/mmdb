pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

include(":features:splash")
include(":app")

include(":base")
include(":base-test")
include(":base-android-test")

include(":features:tmdb")
include(":features:login")
include(":features:home")
include(":features:media")
include(":features:list")
include(":features:base")
include(":features:error")
include(":features:account")

include(":libraries:network")

include(":navigator")
