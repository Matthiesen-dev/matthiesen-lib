plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.api-shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases/")
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft(libs.minecraftNet)
    mappings(loom.officialMojangMappings())
    neoForge(libs.neoforge)

    implementation(project(":api-common", configuration = "namedElements"))
    "developmentNeoForge"(project(":api-common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":api-common", configuration = "transformProductionNeoForge"))
}


tasks {
    processResources {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    sourcesJar {
        val depSources = project(":api-common").tasks.sourcesJar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(depSources)
    }

    shadowJar {
        exclude("fabric.mod.json")
        configurations.set(listOf(shadowBundle))
    }
}
