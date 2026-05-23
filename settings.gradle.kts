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

var workspaceProjectMap = mapOf(
    "api-common" to "api/common",
    "api-fabric" to "api/fabric",
    "api-neoforge" to "api/neoforge",
    "common" to "lib/common",
    "fabric" to "lib/fabric",
    "neoforge" to "lib/neoforge"
)

workspaceProjectMap.forEach { (projectName, projectPath) ->
    run {
        include(projectName)
        project(":$projectName").projectDir = file(projectPath)
    }
}