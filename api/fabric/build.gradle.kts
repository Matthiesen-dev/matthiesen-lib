plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.api-shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val shadowBundle: Configuration by configurations.creating

dependencies {
    // Minecraft & Fabric
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.bundles.fabricModImplementation)
    modCompileOnly(libs.bundles.fabricModCompileOnly)

    // Metrics
    implementation(libs.bundles.faststats)
    shadowBundle(libs.bundles.faststats)

    // Bundle api-common
    implementation(project(":api-common", configuration = "namedElements"))
    "developmentFabric"(project(":api-common", configuration = "namedElements"))
    shadowBundle(project(":api-common", configuration = "transformProductionFabric"))
}


tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations.set(listOf(shadowBundle))
        relocate("dev.faststats", "dev.matthiesen.libs.faststats")
    }
}
