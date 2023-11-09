pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        add(maven {
            url = uri("https://jitpack.io")
        })
    }
}

rootProject.name = "Weight Tracker"
include(":app")
