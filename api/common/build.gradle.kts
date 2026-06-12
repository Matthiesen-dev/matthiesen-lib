plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.api-shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    common("neoforge", "fabric")
}

val shadowBundle: Configuration by configurations.creating

dependencies {
    // Minecraft & Mixins
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.spongeMixin)

    // Metrics
    implementation(libs.faststats)
    shadowBundle(libs.faststats)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("api_mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations.set(listOf(shadowBundle))
    }
}