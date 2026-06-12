plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

evaluationDependsOn(":common")

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
    // Minecraft/ NeoForge Deps
    minecraft(libs.minecraftNet)
    mappings(loom.officialMojangMappings())
    neoForge(libs.neoforge)

    // Ember's Text API (Optional Dep)
    modCompileOnly(libs.emberstextapiNeoForge)
    modRuntimeOnly(libs.emberstextapiNeoForge)

    // Bundle lib-common project
    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))

    // Depend on matthiesen-lib-api mod
    implementation(project(":api-common")) { isTransitive = false }
}


tasks {
    processResources {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    sourcesJar {
        val depSources = project(":common").tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(depSources)
        from(depSources.flatMap { it.archiveFile }.map { zipTree(it) }) {
            exclude("architectury.accessWidener")
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury-common.accessWidener")
        exclude("architectury.common.json")
        configurations.set(listOf(shadowBundle))
    }

    remapJar {
        atAccessWideners.add("matthiesen-lib.accesswidener")
    }
}
