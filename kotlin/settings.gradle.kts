import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    // Only needed to make `intellijPlatform { }` available inside dependencyResolutionManagement.
    // Version is hardcoded because libs.versions.toml isn't initialized yet at this point in settings.
    id("org.jetbrains.intellij.platform.settings") version "2.11.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}

include(":idePlugin")
include(":androidApp")
include(":api")


rootProject.name = "root"