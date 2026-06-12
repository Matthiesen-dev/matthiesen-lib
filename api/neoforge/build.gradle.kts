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

evaluationDependsOn(":api-common")

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
    // Minecraft & NeoForge
    minecraft(libs.minecraftNet)
    mappings(loom.officialMojangMappings())
    neoForge(libs.neoforge)

    // Metrics
    implementation(libs.faststats)
    shadowBundle(libs.faststats)

    // Bundle api-common
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
        val depSources = project(":api-common").tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        dependsOn(depSources)
    }

    shadowJar {
        exclude("fabric.mod.json")
        configurations.set(listOf(shadowBundle))
    }
}
