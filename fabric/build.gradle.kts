plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

val generatedResources = file("src/generated/resources")

sourceSets.main {
    resources {
        srcDir(generatedResources)
    }
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
    modCompileOnly(libs.emberstextapiFabric)
    modRuntimeOnly(libs.emberstextapiFabric)

    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))
    shadowCommon(project(":common", configuration = "transformProductionFabric"))
}


tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        description = "Copies the access widener file to the generated resources directory"
        from(loom.accessWidenerPath)
        into(generatedResources)
    }

    processResources {
        dependsOn(copyAccessWidener)

        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    sourcesJar {
        dependsOn(copyAccessWidener)
    }

    shadowJar {
        configurations.set(listOf(shadowCommon))
    }
}
