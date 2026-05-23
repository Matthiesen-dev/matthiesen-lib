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

val shadowCommon: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric)
    modImplementation(libs.fabricApi)
    modCompileOnly(libs.fabricPermissionsApi)

    implementation(project(":api-common", configuration = "namedElements"))
    "developmentFabric"(project(":api-common", configuration = "namedElements"))
    shadowCommon(project(":api-common", configuration = "transformProductionFabric"))
}


tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations.set(listOf(shadowCommon))
    }
}
