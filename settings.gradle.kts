rootProject.name = "matthiesen-lib"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        gradlePluginPortal()
    }

    includeBuild("gradle/build-logic")
}

listOf(
    "api-shared",
    "common",
    "neoforge",
    "fabric"
).forEach { include(it) }

project(":api-shared").projectDir = file("api/shared")