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
    }
}

rootProject.name = "BanShakingAD"
include(":checks", ":api")
project(":checks").projectDir = File("./api/checks")
project(":api").projectDir = File("./api/api")
include(":app")
